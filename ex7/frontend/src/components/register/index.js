import React, {useState} from 'react';
import Layout from '../layout/Layout';
import UserService from "../../services/UserService";
import { Link } from 'react-router-dom';

const Register = () => {

    const password = useFormInput('');
    const email = useFormInput('');

    return (
        <Layout title="Register" description="This is the Register page" >
            <div className="text-center mt-5">
                <h1>Register</h1>
                <div>
                    Email<br />
                    <input type="text" {...email} autoComplete="new-password" />
                </div>
                <div style={{ marginTop: 10 }}>
                    Password<br />
                    <input type="password" {...password} autoComplete="new-password" />
                </div>

                <div style={{ marginTop: 20 }}>
                    <Link to='/login'>
                        <button className="btn btn-outline-primary btn-sm" onClick={() => UserService.postUser(email.value, password.value)}>
                            Register
                        </button>
                    </Link>
                </div>
            </div>
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

export default Register;