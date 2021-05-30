import React from "react";
import {
  BrowserRouter as Router,
  Switch,
  Route
} from "react-router-dom";

import About from '../components/about/About';
import NotFound from '../components/notFound/NotFound';
import Products from "../components/products";
import Login from "../components/login";
import Register from "../components/register";
import Cart from "../components/cart";

const Routes = () => {
  return (
    <Router>
        <Switch>
          <Route path="/about" component={About} />
          <Route exact path="/" component={Products}/>
          <Route path="/cart" component={Cart} />
          <Route path="/login" component={Login}/>
          <Route path="/register" component={Register}/>
          <Route path="*" component={NotFound} />
        </Switch>
    </Router>
  );
}

export default Routes;