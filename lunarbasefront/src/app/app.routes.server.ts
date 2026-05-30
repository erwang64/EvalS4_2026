import { RenderMode, ServerRoute } from '@angular/ssr';

export const serverRoutes: ServerRoute[] = [
  {
    path: 'bases/detail/:id',
    renderMode: RenderMode.Client
  },
  {
    path: 'crew/detail/:id',
    renderMode: RenderMode.Client
  },
  {
    path: 'equipments/detail/:id',
    renderMode: RenderMode.Client
  },
  {
    path: '**',
    renderMode: RenderMode.Prerender
  }
];
