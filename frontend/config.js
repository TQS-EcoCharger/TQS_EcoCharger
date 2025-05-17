const isDevelopment = window.location.hostname === 'localhost';

const CONFIG = {
    API_URL: isDevelopment 
        ? 'http://localhost:8080/api/'
        : 'adicionar_depois'
};

export default CONFIG;