import React from 'react';
import ReactDOM from 'react-dom';
import Routes from './routes';

import { HelmetProvider } from 'react-helmet-async';
import UserContextProvider from './contexts/LoggedContext';
import CartContextProvider from './contexts/CartContext';
import ProductsContextProvider from "./contexts/ProductsContext";

ReactDOM.render(
    <HelmetProvider>
        <ProductsContextProvider>
            <UserContextProvider>
                <CartContextProvider>
                    <Routes />
                </CartContextProvider>
            </UserContextProvider>
        </ProductsContextProvider>
    </HelmetProvider>,
  document.getElementById('root')
);

