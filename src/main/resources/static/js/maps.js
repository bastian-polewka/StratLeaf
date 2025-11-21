document.addEventListener('DOMContentLoaded', () => {
    const mapDropdown = document.querySelector(".form-select-map");

        if (mapDropdown) {
            mapDropdown.addEventListener('change', (event) => {
                const selectedMap = event.target.value;

                // Check if the user selected "All Maps" (empty value) or a specific map
                if (selectedMap === "") {
                    window.location.href = '/strats'; // Clear filter
                } else {
                    window.location.href = `/strats?mapSelector=${selectedMap}`;
                }
            });
        }
});