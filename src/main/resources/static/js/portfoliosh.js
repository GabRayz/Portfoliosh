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
    outputArea.value += '\n' + message;
    outputArea.rows += (message.match(/\n/g) || []).length + 1;
}

async function sendInput(userInput) {
    display('portfoliosh$ ' + userInput);
    input.value = '';

    window.scroll(0, window.innerHeight);

    if (!stompClient.connected) {
        console.log('Reconnecting...');
        display("Lost connection, reconnecting...");
        await connect();
        display('Connected!');
        display('portfoliosh$ ' + userInput);
    }
    stompClient.send("/sock/send", {}, JSON.stringify({
        'input': userInput
    }));
}

document.querySelector('.input-container').onsubmit = function () {
    sendInput(input.value);

    return false;
}

connect().then(() => sendInput("ls"))

outputArea.value = '';