module.exports = {
  testEnvironment: 'jsdom',
  transform: {
    "^.+\\.(js|jsx)$": "babel-jest",
  },
  moduleNameMapper: {
    "\\.(css|less|scss|sass)$": "identity-obj-proxy",
    "\\.(gif|ttf|eot|svg|png)$": "<rootDir>/__mocks__/fileMock.js" // Si tienes mocks de imágenes
  },
  
  // --- AÑADE O MODIFICA ESTA LÍNEA ---
  setupFilesAfterEnv: ['<rootDir>/src/setupTests.js'],
  // -----------------------------------
};