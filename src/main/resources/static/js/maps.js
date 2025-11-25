document.addEventListener('DOMContentLoaded', () => {
    const mapDropdown = document.querySelector('select[name="mapSelector"]');

    console.log(mapDropdown);

    if (mapDropdown) {
        mapDropdown.addEventListener('change', (event) => {
            const selectedMap = event.target.value;

            console.log(selectedMap);

            // Check if the user selected "All Maps" (empty value) or a specific map
            window.location.href = `/strats?mapSelector=${selectedMap}`;

        });
    }
});