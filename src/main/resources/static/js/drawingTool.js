let lineWidth = 5;
let lineColor = `rgb(${255}, ${255}, ${255})`;
let lineOpacity = 100;


// line color
function linkControls(numberId, rangeId) {
        const numberInput = document.getElementById(numberId);
        const rangeInput = document.getElementById(rangeId);

        // When number input changes → update slider
        numberInput.addEventListener("input", () => {
            rangeInput.value = numberInput.value;
            updateColor();
        });

        // When slider changes → update number input
        rangeInput.addEventListener("input", () => {
            numberInput.value = rangeInput.value;
            updateColor();
        });
    }

function updateColor() {
    const r = document.getElementById("redNumber").value;
    const g = document.getElementById("greenNumber").value;
    const b = document.getElementById("blueNumber").value;
    const a = document.getElementById("lineOpacityNumber").value / 100;

    lineColor = `rgba(${r}, ${g}, ${b}, ${a})`;

    const circle = document.getElementById("colorIndicator");
    circle.style.backgroundColor = `rgba(${r}, ${g}, ${b}, ${a})`;
}

linkControls("redNumber", "redRange");
linkControls("greenNumber", "greenRange");
linkControls("blueNumber", "blueRange");

updateColor();

function setColorCircle(color) {
  document.querySelector('.color-indicator').style.setProperty('--circle-color', color);
}


// line width
const lineWidthNumber = document.getElementById("lineWidthNumber");
const lineWidthRange = document.getElementById("lineWidthRange");

function updateDrawingWidth(value) {
    lineWidth = value;
    lineWidthNumber.value = value;
    lineWidthRange.value = value;
}

lineWidthNumber.addEventListener("input", () => {
    updateDrawingWidth(Number(lineWidthNumber.value));
});

lineWidthRange.addEventListener("input", () => {
    updateDrawingWidth(Number(lineWidthRange.value));
});


// opacity
function updateDrawingOpacity(value) {
    lineOpacity = value;
    lineOpacityNumber.value = value;
    lineOpacityRange.value = value;
    updateColor();
}

lineOpacityNumber.addEventListener("input", () => {
    updateDrawingOpacity(Number(lineWidthNumber.value));
});

lineOpacityRange.addEventListener("input", () => {
    updateDrawingOpacity(Number(lineOpacityRange.value));
});
