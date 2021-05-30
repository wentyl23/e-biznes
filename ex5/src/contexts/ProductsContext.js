import React, { createContext, useState, useEffect } from 'react';
import ProductService from '../services/ProductService';
export const ProductsContext = createContext()

const ProductsContextProvider = ({children}) => {

    const [products, setProducts] = useState([])

    const fetchProducts = async () => {
        const data  = await ProductService.getProducts();
        setProducts(data);
    }

    useEffect(() => {
        fetchProducts();
    }, []);

    return ( 
        <ProductsContext.Provider value={{products}} >
            { children }
        </ProductsContext.Provider>
     );
}
 
export default ProductsContextProvider;