import axios from 'axios';

class UserService {

    async signIn(email, password) {
        return axios.post(`http://localhost:9000/signIn`, {
            email : email,
            password : password
        })
    }

    async signOut(email, password) {
        return axios.post(`http://localhost:9000/signOut`, {
            email : email,
            password : password
        })
    }

    async postUser(email, password) {
        return axios.post('http://localhost:9000/signUp', {
            email : email,
            password : password
        })
    }

    async Oauth(path){
        return axios.get("http://localhost:9000" + path)
    }
}

export default new UserService();