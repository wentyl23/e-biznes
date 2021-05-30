import axios from 'axios';

class UserService {

    async getUser(email, password) {
        return axios.get(`http://localhost:9000/User/${email}/${password}`)
            .then(response => {
                return response.data;
            })
    }

    async postUser(email, login, password) {
        return axios.post('http://localhost:9000/User', {
            email : email,
            login : login,
            password : password
        })
    }
}

export default new UserService();