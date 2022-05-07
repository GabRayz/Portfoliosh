console.log("Hello world!");

const input = document.getElementById('sh-input');
const outputArea = document.getElementById('output');

document.querySelector('.input-container').onsubmit = function () {
    outputArea.value += '\nportfoliosh$ ' + input.value;
    console.log(input.value);
    input.value = '';
    outputArea.rows += 1;

    window.scroll(0, window.innerHeight);
    return false;
}

outputArea.value = '';