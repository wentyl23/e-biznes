import React from 'react';
import Layout from '../layout/Layout';

import ProductsGrid from './ProductsGrid';

const Products = () => {
    
    return ( 
        <Layout title="Whiskey Shop" description="Best in Krakow" >
            <div >
                <div className="text-center mt-5">
                    <h1>Whiskey Shop</h1>
                    <p>Welcome to our store!</p>
                </div>
                <ProductsGrid/>
            </div>
        </Layout>
     );
}
 
export default Products;