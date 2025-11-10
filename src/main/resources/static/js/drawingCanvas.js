const canvas = document.querySelector(".canvas-box");
const ctx = canvas.getContext("2d");
canvas.width = canvas.offsetWidth;
canvas.height = canvas.offsetHeight;

// Drawing
let mode = "move"; // "draw" | "erase" | "move"
let drawing = false;
let currentStroke = [];
const strokes = [];

// Translation and Zoom
let offsetX = 0;
let offsetY = 0;
let scale = 1;
let isPanning = false;
let startPan = { x: 0, y: 0 };


canvas.addEventListener("mousedown", e => {
    if (mode === "move") {
        isPanning = true;
        startPan = { x: e.clientX - offsetX, y: e.clientY - offsetY };
    } else if (mode === "draw" || mode === "erase") {
        drawing = true;
        currentStroke = [{ x: (e.offsetX - offsetX) / scale, y: (e.offsetY - offsetY) / scale }];
    }
});

canvas.addEventListener("mousemove", e => {
    if (isPanning && mode === "move") {
        offsetX = e.clientX - startPan.x;
        offsetY = e.clientY - startPan.y;
        redrawCanvas();
    } else if (drawing && (mode === "draw" || mode === "erase")) {
        const point = { x: (e.offsetX - offsetX) / scale, y: (e.offsetY - offsetY) / scale };
        if (mode === "erase") {
            eraseAtPoint(point);
        } else {
            currentStroke.push(point);
            drawStrokeSegment(currentStroke[currentStroke.length - 2], point, lineColor, lineWidth);
        }
    }
});

canvas.addEventListener("mouseup", () => {
    if (drawing && mode === "draw") {
        strokes.push({ points: currentStroke, color: lineColor, width: lineWidth });
    }
    drawing = false;
    isPanning = false;
});

canvas.addEventListener("wheel", e => {
    e.preventDefault();
    const zoomFactor = 1.1;
    const mouseX = e.offsetX;
    const mouseY = e.offsetY;

    if (e.deltaY < 0) {
        // Zoom in
        scale *= zoomFactor;
        offsetX = mouseX - (mouseX - offsetX) * zoomFactor;
        offsetY = mouseY - (mouseY - offsetY) * zoomFactor;
    } else {
        // Zoom out
        scale /= zoomFactor;
        offsetX = mouseX - (mouseX - offsetX) / zoomFactor;
        offsetY = mouseY - (mouseY - offsetY) / zoomFactor;
    }

    redrawCanvas();
});



function drawStrokeSegment(p1, p2, color, width) {
    ctx.strokeStyle = color;
    ctx.lineWidth = width;
    ctx.beginPath();
    ctx.moveTo(p1.x, p1.y);
    ctx.lineTo(p2.x, p2.y);
    ctx.stroke();
}


function eraseAtPoint(point) {
    for (let i = strokes.length - 1; i >= 0; i--) {
        const stroke = strokes[i];
        if (stroke.points.some(p => distance(p, point) < lineWidth)) {
            strokes.splice(i, 1);
        }
    }
    redrawCanvas();
}


function redrawCanvas() {
    ctx.setTransform(scale, 0, 0, scale, offsetX, offsetY);
    ctx.clearRect(-offsetX / scale, -offsetY / scale, canvas.width / scale, canvas.height / scale);

    if (bgImage.complete) {
        const x = (canvas.width / 2 - bgImage.width / 2);
        const y = (canvas.height / 2 - bgImage.height / 2);
        ctx.drawImage(bgImage, x, y, bgImage.width, bgImage.height);
    }

    for (const stroke of strokes) {
        for (let i = 1; i < stroke.points.length; i++) {
            drawStrokeSegment(stroke.points[i - 1], stroke.points[i], stroke.color, stroke.width);
        }
    }
}

function distance(p1, p2) {
    return Math.sqrt((p1.x - p2.x) ** 2 + (p1.y - p2.y) ** 2);
}


const toolRadios = document.querySelectorAll('input[name="btnRadio"]');

toolRadios.forEach(radio => {
    radio.addEventListener('change', () => {
        if (radio.id === "btnRadioDraw") {
            mode = "draw";
            console.log("Mode: DRAW");
        }
        else if (radio.id === "btnRadioErase") {
            mode = "erase";
            console.log("Mode: ERASE");
        }
        else if (radio.id === "btnRadioMove") {
            mode = "move";
            console.log("Mode: MOVE");
        }
    });
});


const bgImage = new Image();
bgImage.src = "/images/ancient.png";

bgImage.onload = () => {
    redrawCanvas();
};

//TODO: Limit Move
//TODO: Add Undo / Redo
//TODO: Add Stroke smoothing
//TODO: Opacity
//TODO: Line Type, for example dashed
//TODO: Add text
//TODO: Add Nades


