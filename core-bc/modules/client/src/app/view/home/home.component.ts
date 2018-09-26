import { Component, OnInit } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Clinic} from "../../domain/clinic";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  clinics: Clinic[];

  constructor(private http: HttpClient) { }

  ngOnInit() {
    this.http.get<Clinic[]>('/api/clinic').subscribe(clinics => {
      this.clinics = clinics;
    });
  }

}
