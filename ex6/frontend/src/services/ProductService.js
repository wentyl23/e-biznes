import axios from 'axios';

class ProductService {

  async getProducts() {
    return axios.get('http://localhost:9000/Products')
        .then(response => {
          return response.data;
        })
  }
}

export default new ProductService();
