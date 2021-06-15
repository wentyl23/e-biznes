import axios from 'axios';

class UserService {

    async signIn(email, password) {
        return axios.post(`https://whiskeyshop-backend.azurewebsites.net/signIn`, {
            email : email,
            password : password
        })
    }

    async signOut(email, password) {
        return axios.post(`https://whiskeyshop-backend.azurewebsites.net/signOut`, {
            email : email,
            password : password
        })
    }

    async postUser(email, password) {
        return axios.post('https://whiskeyshop-backend.azurewebsites.net/signUp', {
            email : email,
            password : password
        })
    }

    async Oauth(path){
        return axios.get("https://whiskeyshop-backend.azurewebsites.net" + path)
    }
}

export default new UserService();