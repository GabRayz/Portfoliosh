const input = document.getElementById('sh-input');
const outputArea = document.getElementById('output');

let stompClient = null;

function connect() {
    const socket = new SockJS('/websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected!');
        display('Connected!');
        stompClient.subscribe('/sock/receive', function (message) {
            let response = JSON.parse(message.body).input;
            console.log(response);
            if (response.length > 0) {
                display(response);
            }
        })
    })
}

function display(message) {
    outputArea.value += '\n' + message;
    outputArea.rows += (message.match(/\n/g) || []).length + 1;
}

document.querySelector('.input-container').onsubmit = function () {
    const userInput = input.value;
    display('portfoliosh$ ' + userInput);
    console.log(userInput);
    input.value = '';

    window.scroll(0, window.innerHeight);

    if (!stompClient.connected) {
        console.log('Reconnecting...');
        display("Lost connection, reconnecting...");
        connect();
        return;
    }
    stompClient.send("/sock/send", {}, JSON.stringify({
        'input': userInput
    }));

    return false;
}

connect();

outputArea.value = '';