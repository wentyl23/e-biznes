import React, { useContext } from 'react';
import { CartContext } from '../../contexts/CartContext';
import { formatNumber } from '../../helpers/utils';
import whiskyPhoto from '../../Assets/whisky.jpg';

const ProductItem = ({product}) => {

    const { addProduct, cartItems, increase } = useContext(CartContext);

    const isInCart = product => {
        return !!cartItems.find(item => item.id === product.id);
    }

    return ( 
        <div className="card card-body">
            <img style={{display: "block", margin: "0 auto 10px", maxHeight: "200px"}} className="img-fluid" 
            src={whiskyPhoto} alt="whisky"/>
            <p>{product.name}</p>
            <p>{product.description}</p>
            <h3 className="text-left">{formatNumber(product.unitPrice)}</h3>
            <div className="text-right">
                {/*<Link  to="/" className="btn btn-link btn-sm mr-2">Details</Link>*/}

                {
                    isInCart(product) && 
                    <button 
                    onClick={() => increase(product)}
                    className="btn btn-outline-primary btn-sm">Add more</button>
                }

                {
                    !isInCart(product) && 
                    <button 
                    onClick={() => addProduct(product)}
                    className="btn btn-primary btn-sm">Add to cart</button>
                }
                
            </div>
        </div>
     );
}
 
export default ProductItem;