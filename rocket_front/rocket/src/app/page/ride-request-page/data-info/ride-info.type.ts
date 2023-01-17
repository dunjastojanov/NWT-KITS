export class RideInfo {
  public isNow: boolean;
  public time?: string;
  public friends: string[];
  public split: boolean;
  public features: string[];
  public vehicle?: string;
  constructor() {
    this.isNow = false;
    this.split = false;
    this.friends = [];
    this.features = [];
  }
}
