import express from "express";
import path from "path";
import { createServer as createViteServer } from "vite";

async function startServer() {
  const app = express();
  const PORT = Number(process.env.PORT) || 3000;

  // Health check endpoint
  app.get("/api/health", (_req, res) => {
    res.json({ status: "ok", app: "Baytul Ilm AI Portal", version: "1.4.4" });
  });

  // Dedicated Sitemap XML endpoint with proper headers
  app.get("/sitemap.xml", (_req, res) => {
    const sitemapPath = path.resolve(process.cwd(), "public", "sitemap.xml");
    res.header("Content-Type", "application/xml; charset=utf-8");
    res.sendFile(sitemapPath);
  });

  // Dedicated Robots.txt endpoint
  app.get("/robots.txt", (_req, res) => {
    const robotsPath = path.resolve(process.cwd(), "public", "robots.txt");
    res.header("Content-Type", "text/plain; charset=utf-8");
    res.sendFile(robotsPath);
  });

  // Serve static assets from public folder directly with proper headers
  const publicPath = path.resolve(process.cwd(), "public");
  app.use(express.static(publicPath, { maxAge: "1d" }));

  // Vite middleware for development vs static serve for production
  if (process.env.NODE_ENV !== "production") {
    const vite = await createViteServer({
      server: { middlewareMode: true },
      appType: "spa",
    });
    app.use(vite.middlewares);
  } else {
    const distPath = path.resolve(process.cwd(), "dist");
    app.use(express.static(distPath));
    app.use((_req, res) => {
      res.sendFile(path.join(distPath, "index.html"));
    });
  }

  app.listen(PORT, "0.0.0.0", () => {
    console.log(`Server running on http://0.0.0.0:${PORT}`);
  });
}

startServer().catch((err) => {
  console.error("Failed to start server:", err);
  process.exit(1);
});

