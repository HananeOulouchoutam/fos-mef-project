import { Component, EventEmitter, OnInit, Output } from '@angular/core';

interface Event {
  title: string;
  time: string;
  location: string;
  date: Date;
}

@Component({
  selector: 'app-calendar-modal',
  standalone: false,
  templateUrl: './calendar-modal.component.html',
  styleUrl: './calendar-modal.component.css',
})
export class CalendarModalComponent implements OnInit {
  weekDays: string[] = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];
  calendarDays: Date[] = [];
  calendarWeeks: Date[][] = [];
  currentMonth: string = '';
  currentYear: number = 0;
  selectedDate: Date = new Date();
  showModal: boolean = false;
  editingEvent: Event | null = null;

  newEvent: Event = {
    title: '',
    time: '',
    location: '',
    date: new Date(),
  };

  events: Event[] = [];
  selectedDateEvents: Event[] = [];

  ngOnInit() {
    this.generateCalendarDays();
    this.updateCurrentMonthYear();
  }

  generateCalendarDays() {
    const date = new Date();
    const firstDay = new Date(date.getFullYear(), date.getMonth(), 1);
    const lastDay = new Date(date.getFullYear(), date.getMonth() + 1, 0);

    this.calendarDays = [];

    for (let i = 0; i < firstDay.getDay(); i++) {
      const prevDate = new Date(firstDay);
      prevDate.setDate(prevDate.getDate() - (firstDay.getDay() - i));
      this.calendarDays.push(prevDate);
    }

    for (let i = 1; i <= lastDay.getDate(); i++) {
      this.calendarDays.push(new Date(date.getFullYear(), date.getMonth(), i));
    }

    this.calendarWeeks = [];
    let week: Date[] = [];
    this.calendarDays.forEach((day, index) => {
      week.push(day);
      if ((index + 1) % 7 === 0) {
        this.calendarWeeks.push(week);
        week = [];
      }
    });
    if (week.length > 0) {
      this.calendarWeeks.push(week);
    }
  }

  getWeekNumber(date: Date): number {
    const firstDayOfYear = new Date(date.getFullYear(), 0, 1);
    const pastDaysOfYear =
      (date.getTime() - firstDayOfYear.getTime()) / 86400000;
    return Math.ceil((pastDaysOfYear + firstDayOfYear.getDay() + 1) / 7);
  }

  updateCurrentMonthYear() {
    const date = new Date();
    this.currentMonth = date.toLocaleString('default', { month: 'long' });
    this.currentYear = date.getFullYear();
  }

  isCurrentDay(date: Date): boolean {
    const today = new Date();
    return (
      date.getDate() === today.getDate() &&
      date.getMonth() === today.getMonth() &&
      date.getFullYear() === today.getFullYear()
    );
  }

  selectDate(date: Date) {
    this.selectedDate = date;
    this.updateSelectedDateEvents();
  }

  updateSelectedDateEvents() {
    this.selectedDateEvents = this.events.filter(
      (event) =>
        event.date.getDate() === this.selectedDate.getDate() &&
        event.date.getMonth() === this.selectedDate.getMonth() &&
        event.date.getFullYear() === this.selectedDate.getFullYear()
    );
  }

  openEventModal() {
    this.showModal = true;
    this.editingEvent = null;
    this.newEvent = {
      title: '',
      time: '',
      location: '',
      date: this.selectedDate,
    };
  }

  closeModal() {
    this.showModal = false;
  }

  saveEvent() {
    if (this.editingEvent) {
      const index = this.events.indexOf(this.editingEvent);
      this.events[index] = { ...this.newEvent };
    } else {
      this.events.push({ ...this.newEvent });
    }

    this.updateSelectedDateEvents();
    this.closeModal();
  }

  openEventDetails(event: Event) {
    this.editingEvent = event;
    this.newEvent = { ...event };
    this.showModal = true;
  }


  @Output() close = new EventEmitter<void>();
  closeCalendar() {
    this.close.emit();
  }

 
  
}
