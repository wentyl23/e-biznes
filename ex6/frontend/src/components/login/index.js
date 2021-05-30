import React, {useContext, useState} from 'react';
import Layout from '../layout/Layout';
import {LoggedContext} from "../../contexts/LoggedContext";

import {Link} from 'react-router-dom';
import UserService from "../../services/UserService";

const Login = () => {

    const {log_in, logged, user} = useContext(LoggedContext);


    const fetchUser = async (email, password) => {
        const data  = await UserService.getUser(email, password);
        log_in(data);
    }

    const email = useFormInput('');
    const password = useFormInput('');

    return (
        <Layout title="Login" description="This is the Login page">
            {
                logged &&
                <div className="text-center mt-5">
                    <h1>You're already logged as {user.email} !</h1>
                </div>
            }
            {
                !logged &&
                <div className="text-center mt-5">
                    <h1>Log In</h1>
                    <div>
                        Email<br/>
                        <input type="text" {...email} autoComplete="new-password"/>
                    </div>
                    <div style={{marginTop: 10}}>
                        Password<br/>
                        <input type="password" {...password} autoComplete="new-password"/>
                    </div>
                    <div style={{marginTop: 20}}>
                        <Link to='/'>
                            <button className="btn btn-outline-primary btn-sm" onClick={() => {fetchUser(email.value, password.value);}}>
                                Login
                            </button>
                        </Link>
                    </div>
                    <div style={{marginTop: 10}}>
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