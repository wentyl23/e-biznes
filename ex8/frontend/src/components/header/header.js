import React, { useEffect, useContext } from 'react';
import { Link } from "react-router-dom";
import { CartContext } from '../../contexts/CartContext';
import {CartIcon} from '../../Assets';
import styles from './header.module.scss';
import logo from '../../Assets/logo.jpg';
import {LoggedContext} from "../../contexts/LoggedContext";
import Cookies from 'js-cookie';

const Header = () => {

    const {itemCount} = useContext(CartContext);
    const {logged, user, log_out, log_in} = useContext(LoggedContext);

    useEffect(() => {
        const myCookie = Cookies.get('email');

        if (myCookie == null) {
            // do cookie doesn't exist stuff;
        }
        else {
            console.log(myCookie)
            log_in(myCookie)
        }
    }, []);

    const deleteCookie = () => {
        document.cookie.split(";").forEach(function(c) {
            document.cookie = c.replace(/^ +/, "").replace(/=.*/, "=;expires=" + new Date().toUTCString() + ";path=/");
        });
        log_out();
    }

    return (
        <header className={styles.header}>
            <Link to='/'>
                <div>
                    <img src={logo} alt="WhiskeyShop" height="50px" className="logo"/>
                    <span style={{margin: "0px 0px 0px 10px"}}>Products</span>
                </div>
            </Link>
            <Link to='/about'>About</Link>
            <Link to='/cart'> <CartIcon/> Cart ({itemCount})</Link>
            {
                !logged &&
                <Link to='/login'>
                    <button className="btn btn-outline-primary btn-sm">
                        Login
                    </button>
                </Link>
            }
            {
                !logged &&
                <Link to='/register'>
                    <button className="btn btn-outline-primary btn-sm">
                        Register
                    </button>
                </Link>
            }
            {
                logged && <>{user}</>
            }
            {
                logged &&
                <Link to='/'>
                    <button className="btn btn-outline-primary btn-sm" onClick={() => deleteCookie()}>
                        Sign out
                    </button>
                </Link>
            }
        </header>
    );
}
 
export default Header;