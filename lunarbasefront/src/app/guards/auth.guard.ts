import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { ConnectionService } from '../services/connection-service';

export const authGuard: CanActivateFn = () => {
  const connectionService: ConnectionService = inject(ConnectionService);
  const router: Router = inject(Router);

  if (connectionService.connected()) {
    return true;
  } else {
    router.navigateByUrl("connect");
    return false;
  }
};
