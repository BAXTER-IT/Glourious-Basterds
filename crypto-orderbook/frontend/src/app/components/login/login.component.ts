import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  loading = false;
  submitted = false;
  error = '';
  returnUrl: string;
  loginFormElement: any;
  username: string = '';
  password: string = '';

  constructor(
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService
  ) {
    // Redirect to home if already logged in
    if (this.authService.isLoggedIn()) {
      this.router.navigate(['/']);
    }
    
    this.loginForm = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
    
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
  }

  ngOnInit(): void {
  }

  // Convenience getter for easy access to form fields
  get f() { return this.loginForm.controls; }

  onSubmit(): void {
    this.submitted = true;
    console.log('Form submitted', this.loginForm.value);

    // Stop here if form is invalid
    if (this.loginForm.invalid) {
      console.log('Form is invalid', this.loginForm.errors);
      return;
    }

    this.loading = true;
    this.username = this.f['username'].value;
    this.password = this.f['password'].value;
    
    console.log('Attempting login with:', { username: this.username, password: this.password });
    
    this.authService.login(this.username, this.password)
      .subscribe({
        next: (response) => {
          console.log('Login response:', response);
          if (response.success) {
            this.router.navigate([this.returnUrl]);
          } else {
            this.error = response.message || 'Login failed';
            this.loading = false;
          }
        },
        error: (error) => {
          console.error('Login error:', error);
          this.error = 'An error occurred during login';
          this.loading = false;
        }
      });
  }
}