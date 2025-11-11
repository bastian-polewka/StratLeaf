const canvas = document.querySelector(".canvas-box");
const ctx = canvas.getContext("2d");
canvas.width = canvas.offsetWidth;
canvas.height = canvas.offsetHeight;

// Drawing
let mode = "move"; // "draw" | "erase" | "move" | "drag"
let drawing = false;
let currentStroke = [];
const strokes = [];
const undoneStrokes = [];
const objects = [];
let selectedObject = null;

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
    } else if (mode === "drag") {
        const rect = canvas.getBoundingClientRect();
        const mouseX = e.clientX - rect.left;
        const mouseY = e.clientY - rect.top;

        // Check objects from top to bottom (last drawn = top)
        for (let i = objects.length - 1; i >= 0; i--) {
            const obj = objects[i];
            if (
                mouseX > obj.x &&
                mouseX < obj.x + obj.width &&
                mouseY > obj.y &&
                mouseY < obj.y + obj.height
            ) {
                selectedObject = obj;
                offsetX = mouseX - obj.x;
                offsetY = mouseY - obj.y;
                objects.splice(i, 1);
                objects.push(obj);
                break;
            }
        }
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
            redrawCanvas();
            for (let i = 1; i < currentStroke.length; i++) {
                drawStrokeSegment(currentStroke[i - 1], currentStroke[i], lineColor, lineWidth);
            }
        }
    } else if (mode === "drag") {
        if (!selectedObject) return;

        const rect = canvas.getBoundingClientRect();
        const mouseX = e.clientX - rect.left;
        const mouseY = e.clientY - rect.top;

        selectedObject.x = mouseX - offsetX;
        selectedObject.y = mouseY - offsetY;

        redrawCanvas();
    }
});


canvas.addEventListener("mouseup", () => {
    if (drawing && mode === "draw") {
        strokes.push({ points: currentStroke, color: lineColor, width: lineWidth });
    }
    drawing = false;
    isPanning = false;
    selectedObject = null;
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

    objects.forEach(obj => {
        ctx.drawImage(obj.image, obj.x, obj.y, obj.width, obj.height);
    });
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
        else if (radio.id === "btnRadioDrag") {
            mode = "drag";
            console.log("Mode: DRAG");
        }
    });
});


const bgImage = new Image();
bgImage.src = "/images/ancient.png";

bgImage.onload = () => {
    redrawCanvas();
};


// Undo & Redo TODO: undo erased lines
const buttonUndo = document.getElementById("btnUndo");
const buttonRedo = document.getElementById("btnRedo");

buttonUndo.addEventListener("click", function() {
    undoneStrokes.push(strokes.pop());
    redrawCanvas();
});

buttonRedo.addEventListener("click", function() {
    if (undoneStrokes.length > 0) {
        strokes.push(undoneStrokes.pop());
        redrawCanvas();
    }
});


const buttonFlashbang = document.getElementById("btnFlashbang");
const flashbangIcon = new Image();
flashbangIcon.src = "/images/icons/flashbang.svg";

flashbangIcon.onload = () => {
    buttonFlashbang.addEventListener("click", function() {
        objects.push({
            image: flashbangIcon,
            x: 200,
            y: 50,
            width: 400,
            height: 400
        });
        redrawCanvas();
    });
};





/* //Custom cursor
canvas.style.cursor = 'none';
let mouse = { x: 0, y: 0, inside: false };

// track mouse
canvas.addEventListener('mousemove', (e) => {
  const rect = canvas.getBoundingClientRect();
  mouse.x = e.clientX - rect.left;
  mouse.y = e.clientY - rect.top;
  mouse.inside = true;
});

canvas.addEventListener('mouseleave', () => mouse.inside = false);
canvas.addEventListener('mouseenter', () => mouse.inside = true);

function drawCursor(x, y) {
  ctx.beginPath();
  ctx.arc(x, y, 10, 0, Math.PI * 2);  // circle radius = 10
  ctx.fillStyle = 'rgba(255,0,0,0.6)'; // red color with some transparency
  ctx.fill();
  ctx.lineWidth = 2;
  ctx.strokeStyle = 'black'; // optional outline
}

function draw() {
  redrawCanvas();
  if (mouse.inside) drawCursor(mouse.x, mouse.y);

  requestAnimationFrame(draw);
}

draw();
*/

//TODO: Canvas moves while dragging
//TODO: Limit Move, Zoom
//TODO: Add Undo / Redo
//TODO: Add Stroke smoothing
//TODO: Opacity
//TODO: Line Type, for example dashed
//TODO: Add text
//TODO: Add Nades


