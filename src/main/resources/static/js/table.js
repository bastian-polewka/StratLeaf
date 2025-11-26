document.addEventListener('DOMContentLoaded', () => {
    const editableInputs = document.querySelectorAll('.editable-row');

    const deleteButtons = document.querySelectorAll('button[name="deleterButton"]');

    deleteButtons.forEach(button => {
        button.addEventListener("click", e => {
            const row = e.target.closest("tr");
            const stratId = row.dataset.stratId;
            const mapDropdown = document.querySelector('select[name="mapSelector"]');
            const selectedMap = mapDropdown.value;

            const data = {
                id: stratId,
                map: selectedMap
            };

            fetch('/delete-strat', {
                method: 'POST', // maybe change to PUT
                headers: {
                    'Content-Type': 'application/json',
                    // Add CSRF token header here if your framework requires it (e.g., Spring Security)
                },
                body: JSON.stringify(data)
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! Status: ${response.status}`);
                }
                // TODO: If successful, you might update the row visually (e.g., flash a green color)
                console.log(`Deletion successful for ID: ${stratId}`);
                window.location.reload();
            })
            .catch(error => {
                console.error('Deletion failed:', error);
                // TODO: Handle error (e.g., revert the input value or show a message)
            });
        });
    });


    editableInputs.forEach(input => {
        input.addEventListener('change', (event) => {
            const changedInput = event.target;
            const row = changedInput.closest('tr');

            // 1. Get the Strategy ID from the row's data attribute
            const stratId = row.dataset.stratId;


            // TODO: Change so only one element is changed instead of whole row
            let stratData = {};
            row.querySelectorAll('td').forEach(cell => {
                const input = cell.querySelector('input, select');
                if (input) {
                    stratData[input.name] = input.value;
                } else {
                    stratData[cell.dataset.field] = cell.textContent.trim();
                }
            });

            if (!stratId) {
                console.error("Could not find strat ID on the row.");
                return;
            }

            if (changedInput.value.trim() === '') {
                console.warn(`Cannot submit empty field for strat ID: ${stratId}.`);
                return;
            }

            const players = Array.from(document.querySelectorAll('input[name="player"]'))
                .map(nameInput => ({
                    name: nameInput.value,
                    active: true
                }));

            const roleDescriptions = Array.from(row.querySelectorAll('input[name="roleDescription"]'))
                .map(nameInput => nameInput.value);

            const mapDropdown = document.querySelector('select[name="mapSelector"]');
            const selectedMap = mapDropdown.value;

            // 2. Package the data for submission (only the ID and the changed field)
            const data = {
                id: stratId,
                type: stratData.type,
                name: stratData.name,
                description: stratData.description,
                notes: stratData.notes,
                status: stratData.status,
                side: stratData.side,
                players: players,
                roleDescriptions: roleDescriptions,
                map: selectedMap
                //[changedInput.name]: changedInput.value
            };


            console.log(`Sending AJAX update for ID: ${stratId}. Field: ${changedInput.name}`);
            console.log(data);

            // 3. Use the Fetch API to send the data asynchronously
            fetch('/update-strat', {
                method: 'POST', // maybe change to PUT
                headers: {
                    'Content-Type': 'application/json',
                    // Add CSRF token header here if your framework requires it (e.g., Spring Security)
                },
                body: JSON.stringify(data)
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! Status: ${response.status}`);
                }
                // TODO: If successful, you might update the row visually (e.g., flash a green color)
                console.log(`Update successful for ID: ${stratId}`);
                window.location.reload();
            })
            .catch(error => {
                console.error('Update failed:', error);
                // TODO: Handle error (e.g., revert the input value or show a message)
            });
        });
    });
});