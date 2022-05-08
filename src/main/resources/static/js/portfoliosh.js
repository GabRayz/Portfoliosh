const input = document.getElementById('sh-input');
const outputArea = document.getElementById('output');

const socket = new SockJS('/websocket');
const stompClient = Stomp.over(socket);
stompClient.connect({}, function (frame) {
    console.log('Connected!');
    stompClient.subscribe('/sock/receive', function (message) {
        let response = JSON.parse(message.body).input;
        console.log(response);
        if (response.length > 0) {
            outputArea.value += '\n' + response;
            outputArea.rows += (response.match(/\n/g) || []).length + 1;
        }
    })
})

document.querySelector('.input-container').onsubmit = function () {
    const userInput = input.value;
    outputArea.value += '\nportfoliosh$ ' + userInput;
    console.log(userInput);
    input.value = '';
    outputArea.rows += 1;

    window.scroll(0, window.innerHeight);

    stompClient.send("/sock/send", {}, JSON.stringify({
        'input': userInput
    }));

    return false;
}

outputArea.value = '';