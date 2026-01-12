import React, { useState } from 'react'; 
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Navbar from './components/Navbar';
import BooksPage from './pages/BooksPage';
import LoginPage from './pages/LoginPage'; 
import RegistroPage from './pages/RegistroPage';
import ForumPage from './pages/ForumPage'; 

function App() {

  const [isLoggedIn, setIsLoggedIn] = useState(!!localStorage.getItem('token'));

  return (
    <Router>
      <Navbar isLoggedIn={isLoggedIn} setIsLoggedIn={setIsLoggedIn} />
      
      <Routes>
        <Route path="/" element={<BooksPage />} />
        
        <Route path="/login" element={<LoginPage setIsLoggedIn={setIsLoggedIn} />} />
        
        <Route path="/register" element={<RegistroPage />} />
        
        <Route path="/posts" element={<ForumPage />} />
      </Routes>
    </Router>
  );
}

export default App;