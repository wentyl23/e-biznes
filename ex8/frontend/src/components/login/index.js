import React, {useContext, useState} from 'react';
import Layout from '../layout/Layout';
import {LoggedContext} from "../../contexts/LoggedContext";
import {
    GithubLoginButton,
    GoogleLoginButton,
} from "react-social-login-buttons";
import {Link} from 'react-router-dom';
import UserService from "../../services/UserService";

const Login = () => {

    const {log_in, logged, user} = useContext(LoggedContext);


    const fetchUser = async (userEmail, userPassword) => {
        const response = await UserService.signIn(userEmail, userPassword);
        console.log(response);

        log_in(response.data);
    }

    const form_email = useFormInput('');
    const form_password = useFormInput('');

    return (
        <Layout title="Login" description="This is the Login page">
            {
                logged &&
                <div className="text-center mt-5">
                    <h1>You're already logged as {user} !</h1>
                </div>
            }
            {
                !logged &&
                <div className="text-center mt-5">
                    <h1>Log In</h1>
                    <div>
                        Email<br/>
                        <input type="text" {...form_email} autoComplete="new-password"/>
                    </div>
                    <div style={{marginTop: 10}}>
                        Password<br/>
                        <input type="password" {...form_password} autoComplete="new-password"/>
                    </div>
                    <div style={{marginTop: 20}}>
                        <Link to='/'>
                            <button className="btn btn-outline-primary btn-sm" onClick={() => {fetchUser(form_email.value, form_password.value);}}>
                                Login
                            </button>
                        </Link>
                    </div>
                    <h2 style={{marginTop: 10}}>Or</h2>
                    <div style={{marginTop: 10, display: 'flex', justifyContent: 'center', allignItems: 'center'}}>
                        <Link to='/'>
                            <GoogleLoginButton
                                onClick={() => window.location.assign("https://whiskeyshop-backend.azurewebsites.net/authenticate/google")}
                            />
                        </Link>
                    </div>
                    <div style={{marginTop: 10, display: 'flex', justifyContent: 'center', allignItems: 'center'}}>
                        <Link to='/login'>
                            <GithubLoginButton
                                               onClick={() => window.location.assign("https://whiskeyshop-backend.azurewebsites.net/authenticate/github")}
                            />
                        </Link>
                    </div>
                    <div style={{marginTop: 20}}>
                        <Link to='/register'>
                            Don't have account yet ? Register here
                        </Link>
                    </div>
                </div>
            }
        </Layout>
    );
}

const useFormInput = initialValue => {
    const [value, setValue] = useState(initialValue);

    const handleChange = e => {
        setValue(e.target.value);
    }
    return {
        value,
        onChange: handleChange
    }
}

export default Login;