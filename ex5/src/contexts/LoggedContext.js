import React, { createContext, useReducer} from 'react';
import { LoggerReducer, saveUser} from './LoggerReducer';

export const LoggedContext = createContext()

const storage = localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')) : {};
const initialState = { logged: false, user : saveUser(storage)};

const UserContextProvider = ({children}) => {

    const [state, dispatch] = useReducer(LoggerReducer, initialState)

    const log_in = payload => {
        dispatch({type: 'LOG_IN', payload})
    }

    const log_out = () => {
        dispatch({type: 'LOG_OUT'})
    }

    const contextValues = {
        log_in,
        log_out,
        ...state
    }

    return (
        <LoggedContext.Provider value={contextValues} >
            { children }
        </LoggedContext.Provider>
    );
}

export default UserContextProvider;