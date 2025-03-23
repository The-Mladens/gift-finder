const isProduction = window.location.hostname !== "localhost" && window.location.hostname !== "127.0.0.1";

export const CONFIG = {
    apiUrl: isProduction
        ? "the-mladens-gift-finder-dev.azurewebsites.net/api/MessageFunctionTest"
        : "http://localhost:7071/api/MessageFunctionTest",
};