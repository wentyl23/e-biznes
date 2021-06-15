
export const saveUser = user => {
    localStorage.setItem('user', JSON.stringify(user));
    return user
}

export const LoggedReducer = (state, action) => {
    switch (action.type) {
        case "LOG_IN":
            return {
                logged: true,
                user: saveUser(action.payload)
            }
        case "LOG_OUT":
            return {
                logged: false,
                user: saveUser({})
            }

        default:
            return state

    }
}