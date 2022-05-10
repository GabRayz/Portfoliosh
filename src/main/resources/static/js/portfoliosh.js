const input = document.getElementById('sh-input');
const outputArea = document.getElementById('output');

let stompClient = null;

function connect() {
    return new Promise((resolve, reject) => {
        const socket = new SockJS('/websocket');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            console.log('Connected!');
            stompClient.subscribe('/sock/receive', function (message) {
                let response = JSON.parse(message.body).input;
                console.log(response);
                if (response.length > 0) {
                    display(response);
                }
            })
            resolve();
        })
    });
}

function display(message) {
    if (message.endsWith('\n'))
        message = message.substring(0, message.length - 1);
    outputArea.value += '\n' + message;
    outputArea.rows += (message.match(/\n/g) || []).length + 1;
    window.scroll(0, document.body.scrollHeight);
}

let history = [];
let historyIndex = 0;

async function sendInput(userInput) {
    display('portfoliosh$ ' + userInput);
    input.value = '';

    if (!stompClient.connected) {
        console.log('Reconnecting...');
        display("Lost connection, reconnecting...");
        await connect();
        display('Connected!');
        display('portfoliosh$ ' + userInput);
    }
    if (userInput !== '') {
        history.push(userInput);
        historyIndex = history.length;
    }
    stompClient.send("/sock/send", {}, JSON.stringify({
        'input': userInput
    }));
}

document.querySelector('.input-container').onsubmit = function () {
    if (input.value === 'clear') {
        outputArea.value = '';
        outputArea.rows = 1;
        input.value = '';
        return false;
    }
    sendInput(input.value);

    return false;
}

document.addEventListener('keydown', function (e) {
    if (e.key === 'ArrowUp') {
        historyIndex--;
        if (historyIndex < 0)
            historyIndex = 0;
        restoreHistory();
    } else if (e.key === 'ArrowDown') {
        historyIndex++;
        if (historyIndex >= history.length)
            historyIndex = history.length;
        restoreHistory();
    }
});

function restoreHistory() {
    if (historyIndex === history.length)
        input.value = '';
    else
        input.value = history[historyIndex]
}

connect().then(() => sendInput("cat welcome.md"))

outputArea.value = '';