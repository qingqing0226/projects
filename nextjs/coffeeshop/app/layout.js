export default function RootLayout({ children }) {
  return (
    <html>
      <head>
        <title>Sött & Elegant - Hemsida</title>
        <link href="styles.css" type="text/css" rel="stylesheet" />
	      <link rel="stylesheet" media="screen" href="https://fontlibrary.org/face/cursive" type="text/css"/>
      </head>
      <body>
      <header>
	    <h1>Sött & Elegant</h1>		
      <nav>
        <a href="hemsida.html" class="nav-item">STARTSIDA</a>
        <a href="sortiment.html" class="nav-item">SORTIMENT</a>
        <a href="bestallning.html" class="nav-item">BESTÄLLNING</a>
        <a href="cafe.html" class="nav-item">CAFÉ</a>
        <a href="kontakt.html" class="nav-item">KONTAKT</a>
        <a href="personal.html">PERSONAL</a>
      </nav>
      <hr />
      </header>
        {children}
      </body>
    </html>
  )
}
