import React, { useEffect, useState } from 'react';
import { getAllPosts, createPost, deletePost, getCommentsByPostId, createComment } from '../services/api'; 
import './Login.css';
import './Forum.css';

const ForumPage = () => {
    const [posts, setPosts] = useState([]);
    const [newPost, setNewPost] = useState({ title: '', content: '' });
    const [isLoading, setIsLoading] = useState(false);

    const [activePostId, setActivePostId] = useState(null); 
    const [comments, setComments] = useState([]); 
    const [newComment, setNewComment] = useState(''); 

    useEffect(() => {
        loadPosts();
    }, []);

    const loadPosts = async () => {
        try {
            const data = await getAllPosts();
            if (Array.isArray(data)) setPosts(data);
            else setPosts([]);
        } catch (error) {
            setPosts([]);
        }
    };

    const toggleComments = async (postId) => {
        if (activePostId === postId) {
            setActivePostId(null);
            setComments([]);
        } else {
            setActivePostId(postId);
            try {
                const data = await getCommentsByPostId(postId);
                setComments(Array.isArray(data) ? data : []);
            } catch (error) {
                console.error("Error cargando comentarios", error);
            }
        }
    };

    const handleCommentSubmit = async (e, postId) => {
        e.preventDefault();
        const userEmail = localStorage.getItem('email');
        if (!userEmail) return alert("Inicia sesión para comentar");

        try {
            await createComment({
                content: newComment,
                email: userEmail,
                postId: postId
            });
            setNewComment(''); 
            const data = await getCommentsByPostId(postId);
            setComments(data);
        } catch (error) {
            alert("Error al enviar comentario");
        }
    };

    const handleCreate = async (e) => {
        e.preventDefault();
        if (!newPost.title || !newPost.content) return;
        const userEmail = localStorage.getItem('email');
        if (!userEmail) { alert("Debes iniciar sesión"); return; }
        setIsLoading(true);
        try {
            await createPost({ title: newPost.title, content: newPost.content, email: userEmail });
            setNewPost({ title: '', content: '' });
            loadPosts(); 
        } catch (error) { console.error(error); } finally { setIsLoading(false); }
    };

    const handleDelete = async (id) => {
        if (window.confirm('¿Borrar post?')) {
            try { await deletePost(id); loadPosts(); } catch (error) { alert('Error'); }
        }
    };

    return (
        <div className="forum-container">
            <div className="forum-header login-card">
                <h1>Foro de la Comunidad</h1>
                <p>Comparte tus ideas o preguntas sobre libros</p>
                <form onSubmit={handleCreate} className="forum-form">
                    <input type="text" placeholder="Título..." value={newPost.title} onChange={(e) => setNewPost({...newPost, title: e.target.value})} className="forum-input" />
                    <textarea placeholder="Contenido..." value={newPost.content} onChange={(e) => setNewPost({...newPost, content: e.target.value})} className="forum-textarea" />
                    <button type="submit" className="login-btn" disabled={isLoading}>{isLoading ? '...' : 'Publicar'}</button>
                </form>
            </div>

            <div className="posts-list">
                {!Array.isArray(posts) || posts.length === 0 ? (
                    <p style={{textAlign: 'center'}}>No hay discusiones.</p>
                ) : (
                    posts.map((post) => (
                        <div key={post.id} className="post-card-wrapper">
                            <div className="post-card">
                                <div className="post-content">
                                    <h3>{post.title}</h3>
                                    <p>{post.content}</p>
                                    <small style={{color: '#888'}}>Por: {post.author?.username || 'Anónimo'}</small>
                                </div>
                                <div className="post-actions">
                                    <button 
                                        className="action-btn comment-btn"
                                        onClick={() => toggleComments(post.id)}
                                    >
                                        {activePostId === post.id ? 'Ocultar' : 'Comentarios'}
                                    </button>
                                    <button className="action-btn delete-btn" onClick={() => handleDelete(post.id)}>Borrar</button>
                                </div>
                            </div>

                            {activePostId === post.id && (
                                <div className="comments-section">
                                    {comments.map(c => (
                                        <div key={c.id} className="comment-bubble">
                                            <strong>{c.author?.username}:</strong> {c.content}
                                        </div>
                                    ))}
                                    
                                    <form onSubmit={(e) => handleCommentSubmit(e, post.id)} className="comment-form">
                                        <input 
                                            type="text" 
                                            placeholder="Escribe una respuesta..."
                                            value={newComment}
                                            onChange={(e) => setNewComment(e.target.value)}
                                        />
                                        <button type="submit">Enviar</button>
                                    </form>
                                </div>
                            )}
                        </div>
                    ))
                )}
            </div>
        </div>
    );
};

export default ForumPage;