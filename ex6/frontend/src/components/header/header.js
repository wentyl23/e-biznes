import React, { useContext } from 'react';
import { Link } from "react-router-dom";
import { CartContext } from '../../contexts/CartContext';
import {CartIcon} from '../../Assets';
import styles from './header.module.scss';
import logo from '../../Assets/logo.jpg';
import {LoggedContext} from "../../contexts/LoggedContext";

const Header = () => {

    const {itemCount} = useContext(CartContext);
    const {logged, user, log_out} = useContext(LoggedContext);

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
                logged && <>{user.email}</>
            }
            {
                logged &&
                <Link to='/'>
                    <button className="btn btn-outline-primary btn-sm" onClick={() => log_out()}>
                        Sign out
                    </button>
                </Link>
            }
        </header>
    );
}
 
export default Header;