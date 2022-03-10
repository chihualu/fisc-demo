function waterMarkCreate(waterMarkStr, target) {
    var canvas = document.createElement("canvas");
    var fontSize = 24;
    var height = "200";
    canvas.setAttribute('height', "200");
    canvas.setAttribute('width', "400");
    var context = canvas.getContext('2d');
    context.fillStyle = 'rgba(0, 0, 0, 0.1)';
    context.font = fontSize + 'px sans-serif';
    context.rotate(-20 * Math.PI / 180);
    context.fillText(waterMarkStr, 0, height);

    $(target).css('-webkit-print-color-adjust', 'exact !important');
    $(target).css('background-image', "url(" + canvas.toDataURL("image/png") + ")");
}