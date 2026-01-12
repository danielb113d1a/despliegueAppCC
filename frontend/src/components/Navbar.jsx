import React, { useState, useEffect } from 'react';
import { AppBar, Toolbar, Typography, Button, Box, Menu, MenuItem, IconButton } from '@mui/material';
import { Link, useNavigate } from 'react-router-dom';
import LibraryBooksIcon from '@mui/icons-material/LibraryBooks';
import AccountCircle from '@mui/icons-material/AccountCircle';

const Navbar = ({ isLoggedIn, setIsLoggedIn }) => {
  const navigate = useNavigate();
  
  const [anchorEl, setAnchorEl] = useState(null);
  
  const userEmail = localStorage.getItem('email') || 'Usuario';

  const handleMenu = (event) => {
    setAnchorEl(event.currentTarget);
  };

  const handleClose = () => {
    setAnchorEl(null);
  };

  const handleLogout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('email');
    
    if (setIsLoggedIn) setIsLoggedIn(false);
    
    handleClose();
    
    navigate('/');
  };

  return (
    <AppBar position="static">
      <Toolbar>
        <LibraryBooksIcon sx={{ mr: 2 }} />
        
        <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
          <Link to="/" style={{ color: 'white', textDecoration: 'none' }}>
            CloudLibrary
          </Link>
        </Typography>

        <Box>
          <Button color="inherit" component={Link} to="/">Libros</Button>
          <Button color="inherit" component={Link} to="/posts">Foro</Button>

          {isLoggedIn ? (
            <>
              <Button 
                color="inherit" 
                onClick={handleMenu}
                startIcon={<AccountCircle />}
                sx={{ textTransform: 'none', fontWeight: 'bold' }}
              >
                {userEmail}
              </Button>

              <Menu
                id="menu-appbar"
                anchorEl={anchorEl}
                anchorOrigin={{
                  vertical: 'bottom', 
                  horizontal: 'right',
                }}
                keepMounted
                transformOrigin={{
                  vertical: 'top',
                  horizontal: 'right',
                }}
                open={Boolean(anchorEl)}
                onClose={handleClose}
              >
                <MenuItem disabled>Mi Perfil</MenuItem>
                <MenuItem 
                    onClick={handleLogout} 
                    sx={{ color: 'error.main', fontWeight: 'bold' }}
                >
                    Cerrar Sesión
                </MenuItem>
              </Menu>
            </>
          ) : (
            <>
              <Button color="inherit" component={Link} to="/login">Login</Button>
              <Button color="inherit" component={Link} to="/register">Registro</Button>
            </>
          )}
        </Box>
      </Toolbar>
    </AppBar>
  );
};

export default Navbar;