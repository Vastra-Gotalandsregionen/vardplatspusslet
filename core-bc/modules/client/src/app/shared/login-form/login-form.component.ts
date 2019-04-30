import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {AuthService} from "../../service/auth.service";
import {ActivatedRoute, Router} from "@angular/router";
import {TokenResponse} from '../../domain/token-response';

@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.scss']
})
export class LoginFormComponent implements OnInit {

  userId: string;
  password: string;

  returnUrl: string;

  constructor(private http: HttpClient,
              private route: ActivatedRoute,
              private router: Router,
              private authService: AuthService) { }

  ngOnInit() {
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
  }

  login() {
    this.http.post('/api/login', {username: this.userId, password: this.password})
      .subscribe((response: TokenResponse) => {
        this.authService.jwt = response.token;

        if (response) {
          this.router.navigateByUrl(this.returnUrl);
        }
      });
  }

}
