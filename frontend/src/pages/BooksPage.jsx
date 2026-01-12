import React, { useEffect, useState } from 'react';
import { getAllBooks, getAllCategories, addRating, createBook, getUserRatings, deleteRating, deleteBook } from '../services/api';
import './Login.css';
import './Books.css';

const BooksPage = () => {
  const [books, setBooks] = useState([]);
  const [categories, setCategories] = useState([]);
  const [selectedCategory, setSelectedCategory] = useState(null);

  const [userRatings, setUserRatings] = useState({});

  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [showForm, setShowForm] = useState(false);
  const [newBook, setNewBook] = useState({ title: '', author: '', description: '' });

  const currentUserEmail = localStorage.getItem('email');

  useEffect(() => {
    loadData();
    const token = localStorage.getItem('token');
    setIsLoggedIn(token && token !== "undefined");
  }, []);

  const loadData = async () => {
    try {
      const [booksData, catsData] = await Promise.all([getAllBooks(), getAllCategories()]);
      setBooks(Array.isArray(booksData) ? booksData : []);
      setCategories(Array.isArray(catsData) ? catsData : []);

      const email = localStorage.getItem('email');
      if (email) {
        const myRatings = await getUserRatings(email);
        const ratingsMap = {};
        if (Array.isArray(myRatings)) {
          myRatings.forEach(r => {
            if (r.book && r.book.id) {
              ratingsMap[r.book.id] = { id: r.id, value: r.value };
            }
          });
        }
        setUserRatings(ratingsMap);
      }
    } catch (error) { console.error(error); }
  };

  const handleInputChange = (e) => {
    setNewBook({ ...newBook, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await createBook({ ...newBook, email: currentUserEmail });
      alert('Libro añadido con éxito');
      setShowForm(false);
      setNewBook({ title: '', author: '', description: '' });
      loadData();
    } catch (error) {
      console.error(error);
      alert("Error al crear el libro.");
    }
  };

  const handleRate = async (bookId, stars) => {
    const email = localStorage.getItem('email');
    if (!email) return alert("Inicia sesión para valorar");
    try {
      await addRating({ value: stars, email, bookId });
      loadData(); 
    } catch (error) { alert("Error al valorar"); }
  };

  const handleDeleteVote = async (ratingId) => {
    if (!window.confirm("¿Quieres eliminar tu valoración?")) return;
    try {
      await deleteRating(ratingId);
      loadData();
    } catch (error) {
      console.error(error);
      alert("Error al eliminar el voto");
    }
  };

  const handleDeleteBook = async (bookId) => {
    if (!window.confirm("¿Seguro que quieres borrar este libro permanentemente?")) return;
    try {
      await deleteBook(bookId);
      loadData();
    } catch (error) {
      console.error(error);
      alert("Error al eliminar el libro");
    }
  };

  const filteredBooks = selectedCategory
    ? books.filter(b => b.category?.id === selectedCategory)
    : books;

  return (
    <div className="books-container">
      <div className="books-header">
        <h1>Biblioteca CloudLibrary</h1>
        <p>Explora nuestra colección de lecturas</p>

        <div style={{ marginBottom: '20px', marginTop: '20px' }}>
          <button
            onClick={() => isLoggedIn && setShowForm(!showForm)}
            disabled={!isLoggedIn}
            className={isLoggedIn ? 'add-book-btn' : 'btn-disabled'}
            title={!isLoggedIn ? "Inicia sesión para añadir" : ""}
          >
            {showForm ? '✖ Cerrar Formulario' : '+ Añadir Nuevo Libro'}
          </button>
          {!isLoggedIn && <p style={{ fontSize: '0.8rem', color: '#666' }}>Inicia sesión para añadir libros</p>}
        </div>

        {showForm && (
          <div className="book-form-card">
            <h3>Nuevo Libro</h3>
            <form onSubmit={handleSubmit} className="form-inline-style">
              <input
                type="text"
                name="title"
                placeholder="Título"
                value={newBook.title}
                onChange={handleInputChange}
                required
              />
              <input
                type="text"
                name="author"
                placeholder="Autor"
                value={newBook.author}
                onChange={handleInputChange}
                required
              />

              <select
                name="categoryId"
                value={newBook.categoryId || ''}
                onChange={handleInputChange}
                required
                style={{ width: '100%', padding: '10px', marginBottom: '10px', borderRadius: '6px', border: '1px solid #ddd' }}
              >
                <option value="">-- Selecciona una Categoría --</option>
                {categories.map(cat => (
                  <option key={cat.id} value={cat.id}>
                    {cat.name}
                  </option>
                ))}
              </select>

              <textarea
                name="description"
                placeholder="Descripción..."
                value={newBook.description}
                onChange={handleInputChange}
                rows="3"
                style={{ width: '100%', marginTop: '10px', padding: '8px' }}
              />

              <div style={{ marginTop: '15px', display: 'flex', gap: '10px' }}>
                <button type="submit" className="btn-save">Guardar Libro</button>
                <button type="button" onClick={() => setShowForm(false)} className="btn-cancel">Cancelar</button>
              </div>
            </form>
          </div>
        )}

        <div className="categories-scroll">
          <button className={`cat-pill ${selectedCategory === null ? 'active' : ''}`} onClick={() => setSelectedCategory(null)}>Todos</button>
          {categories.map(cat => (
            <button key={cat.id} className={`cat-pill ${selectedCategory === cat.id ? 'active' : ''}`} onClick={() => setSelectedCategory(cat.id)}>{cat.name}</button>
          ))}
        </div>
      </div>

      <div className="books-grid">
        {filteredBooks.length === 0 ? (
          <p className="no-books">No hay libros disponibles.</p>
        ) : (
          filteredBooks.map((book) => {
            const userRatingData = userRatings[book.id];
            const userVote = userRatingData ? userRatingData.value : 0;
            const ratingId = userRatingData ? userRatingData.id : null;

            const isOwner = book.user && book.user.email === currentUserEmail;

            return (
              <div key={book.id} className="book-card">
                {isOwner && (
                  <button
                    className="delete-book-btn"
                    onClick={() => handleDeleteBook(book.id)}
                    title="Borrar mi libro"
                  >
                    ✕
                  </button>
                )}

                <div className="book-cover">
                  <span>{book.title ? book.title.charAt(0) : 'L'}</span>
                </div>

                <div className="book-info">
                  <h3>{book.title}</h3>
                  <p className="book-author">Por: {book.author || 'Autor desconocido'}</p>

                  <div style={{ marginBottom: '10px', fontSize: '0.9rem', color: '#666' }}>
                    <span style={{ color: '#fbbf24', fontWeight: 'bold', fontSize: '1.1rem' }}>
                      ★ {book.averageRating ? book.averageRating.toFixed(1) : '0.0'}
                    </span>
                    <span style={{ marginLeft: '5px' }}>
                      ({book.totalRatings || 0} valoraciones)
                    </span>
                  </div>

                  <p className="book-desc">{book.description || 'Sin descripción'}</p>

                  <div className="rating-area">
                    <div style={{ display: 'flex', alignItems: 'center' }}>
                      <span>{userVote > 0 ? 'Tu voto:' : 'Valorar:'} </span>
                      {[1, 2, 3, 4, 5].map(star => (
                        <span
                          key={star}
                          className={`star ${star <= userVote ? 'filled' : ''}`}
                          onClick={() => handleRate(book.id, star)}
                        >
                          ★
                        </span>
                      ))}

                      {ratingId && (
                        <button
                          onClick={() => handleDeleteVote(ratingId)}
                          className="delete-rating-btn"
                          title="Eliminar mi valoración"
                        >
                          🗑️
                        </button>
                      )}
                    </div>
                  </div>
                </div>
              </div>
            );
          })
        )}
      </div>
    </div>
  );
};

export default BooksPage;