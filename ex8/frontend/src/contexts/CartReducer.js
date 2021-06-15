
const Storage = (cartItems) => {
    localStorage.setItem('cart', JSON.stringify(cartItems.length > 0 ? cartItems: []));
}

export const sumItems = cartItems => {
    Storage(cartItems);
    let itemCount = cartItems.reduce((totalItems, product) => totalItems + product.quantity, 0);
    let total = cartItems.reduce((price, product) => price + product.unitPrice * product.quantity, 0).toFixed(2);
    return { itemCount, total }
}

const decrease = (cartItems, id) => {
    return cartItems.map(x => {
        if (x.id !== id) {
            return x
        }

        return {
            ...x,
            quantity: x.quantity-1
        }
    })
}

const increase = (cartItems, id) => {
    return cartItems.map(x => {
        if (x.id !== id) {
            return x
        }

        return {
            ...x,
            quantity: x.quantity+1
        }
    })
}

export const CartReducer = (state, action) => {
    switch (action.type) {
        case "ADD_ITEM":
            if (!state.cartItems.find(item => item.id === action.payload.id)) {
                return {
                    ...state,
                    ...sumItems([...state.cartItems, {...action.payload, quantity: 1}]),
                    cartItems: [
                        ...state.cartItems,
                        {
                            ...action.payload,
                            quantity: 1
                        }
                    ]
                }
            } 

            return {
                ...state,
                ...sumItems(state.cartItems),
                cartItems: [...state.cartItems]
            }
        case "REMOVE_ITEM":
            return {
                ...state,
                ...sumItems(state.cartItems.filter(item => item.id !== action.payload.id)),
                cartItems: [...state.cartItems.filter(item => item.id !== action.payload.id)]
            }
        case "INCREASE":
            return {
                ...state,
                ...sumItems(increase(state.cartItems, action.payload.id)),
                cartItems: [...increase(state.cartItems, action.payload.id)]
            }
        case "DECREASE":
            return {
                ...state,
                ...sumItems(decrease(state.cartItems, action.payload.id)),
                cartItems: [...decrease(state.cartItems, action.payload.id)]
            }
        case "CHECKOUT":
            return {
                cartItems: [],
                checkout: true,
                ...sumItems([]),
            }
        case "CLEAR":
                return {
                    cartItems: [],
                    checkout: false,
                    ...sumItems([]),
                }
        default:
            return state

    }
}