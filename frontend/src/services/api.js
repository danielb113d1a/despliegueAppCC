import axios from 'axios';

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';

const api = axios.create({
    baseURL: API_URL,
});

export const registerUser = async (userData) => {
    const response = await api.post('/users/register', userData);
    return response.data;
};

export const loginUser = async (credentials) => {
    const response = await api.post('/users/login', credentials);
    return response.data; 
};

export const getBooks = async () => {
    const token = localStorage.getItem('token');
    const config = token ? { headers: { Authorization: `Bearer ${token}` } } : {};
    const response = await api.get('/books', config);
    return response.data;
};

export const getAllPosts = async () => {
    const response = await api.get('/posts');
    return response.data;
};

export const createPost = async (postData) => {
    const response = await api.post('/posts', postData);
    return response.data;
};

export const deletePost = async (id) => {
    await api.delete(`/posts/${id}`);
};

export const getCommentsByPostId = async (postId) => {
    const response = await api.get(`/comments/post/${postId}`);
    return response.data;
};

export const createComment = async (commentData) => {
    const response = await api.post('/comments', commentData);
    return response.data;
};

export const getAllBooks = async () => {
    const response = await api.get('/books');
    return response.data;
};

export const getAllCategories = async () => {
    const response = await api.get('/categories');
    return response.data;
};

export const addRating = async (ratingData) => {
    const response = await api.post('/ratings', ratingData);
    return response.data;
};

export const getRatingsByBookId = async (bookId) => {
    const response = await api.get(`/ratings/book/${bookId}`);
    return response.data;
};

export const getUserRatings = async (email) => {
    const response = await api.get(`/ratings/user/${email}`);
    return response.data;
};

export const deleteRating = async (ratingId) => {
    await api.delete(`/ratings/${ratingId}`);
};

export const createBook = async (bookData) => {
    const response = await api.post('/books', bookData);
    return response.data;
};

export const deleteBook = async (id) => {
    await api.delete(`/books/${id}`);
};

export default api;