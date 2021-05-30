import React from 'react';
import Layout from '../layout/Layout';
import logo from '../../Assets/logo2.jpg';

const About = () => {
    
    return ( 
        <Layout title="About" description="This is the About page" >
            <div className="text-center mt-5">
                <div>
                    <img src={logo} alt="WhiskeyShop" className="logo"/>
                </div>
                <div style={{ marginTop: 10 }}>
                    <h1>About</h1>
                    <p>Our company is a small business. We try our best to satisfy our customers by commiting to our work every day!</p>
                </div>
            </div>
        </Layout>
     );
}
 
export default About;