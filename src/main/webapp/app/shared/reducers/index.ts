import { combineReducers } from 'redux';
import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';

import locale, { LocaleState } from './locale';
import authentication, { AuthenticationState } from './authentication';
import applicationProfile, { ApplicationProfileState } from './application-profile';

import administration, { AdministrationState } from 'app/modules/administration/administration.reducer';
import userManagement, { UserManagementState } from 'app/modules/administration/user-management/user-management.reducer';
import register, { RegisterState } from 'app/modules/account/register/register.reducer';
import activate, { ActivateState } from 'app/modules/account/activate/activate.reducer';
import password, { PasswordState } from 'app/modules/account/password/password.reducer';
import settings, { SettingsState } from 'app/modules/account/settings/settings.reducer';
import passwordReset, { PasswordResetState } from 'app/modules/account/password-reset/password-reset.reducer';
// prettier-ignore
import region, {
  RegionState
} from 'app/entities/region/region.reducer';
// prettier-ignore
import city, {
  CityState
} from 'app/entities/city/city.reducer';
// prettier-ignore
import district, {
  DistrictState
} from 'app/entities/district/district.reducer';
// prettier-ignore
import street, {
  StreetState
} from 'app/entities/street/street.reducer';
// prettier-ignore
import house, {
  HouseState
} from 'app/entities/house/house.reducer';
// prettier-ignore
import serviceFee, {
  ServiceFeeState
} from 'app/entities/service-fee/service-fee.reducer';
// prettier-ignore
import houseTracking, {
  HouseTrackingState
} from 'app/entities/house-tracking/house-tracking.reducer';
// prettier-ignore
import housePhoto, {
  HousePhotoState
} from 'app/entities/house-photo/house-photo.reducer';
// prettier-ignore
import landProject, {
  LandProjectState
} from 'app/entities/land-project/land-project.reducer';
// prettier-ignore
import landProjectPhoto, {
  LandProjectPhotoState
} from 'app/entities/land-project-photo/land-project-photo.reducer';
// prettier-ignore
import article, {
  ArticleState
} from 'app/entities/article/article.reducer';
// prettier-ignore
import category, {
  CategoryState
} from 'app/entities/category/category.reducer';
// prettier-ignore
import userProfile, {
  UserProfileState
} from 'app/entities/user-profile/user-profile.reducer';
// prettier-ignore
import userSubscription, {
  UserSubscriptionState
} from 'app/entities/user-subscription/user-subscription.reducer';
// prettier-ignore
import userTracking, {
  UserTrackingState
} from 'app/entities/user-tracking/user-tracking.reducer';
// prettier-ignore
import userFeed, {
  UserFeedState
} from 'app/entities/user-feed/user-feed.reducer';
// prettier-ignore
import searchTracking, {
  SearchTrackingState
} from 'app/entities/search-tracking/search-tracking.reducer';
// prettier-ignore
import userFinancial, {
  UserFinancialState
} from 'app/entities/user-financial/user-financial.reducer';
// prettier-ignore
import potentialCustomer, {
  PotentialCustomerState
} from 'app/entities/potential-customer/potential-customer.reducer';
// prettier-ignore
import banner, {
  BannerState
} from 'app/entities/banner/banner.reducer';
// prettier-ignore
import payment, {
  PaymentState
} from 'app/entities/payment/payment.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

export interface IRootState {
  readonly authentication: AuthenticationState;
  readonly locale: LocaleState;
  readonly applicationProfile: ApplicationProfileState;
  readonly administration: AdministrationState;
  readonly userManagement: UserManagementState;
  readonly register: RegisterState;
  readonly activate: ActivateState;
  readonly passwordReset: PasswordResetState;
  readonly password: PasswordState;
  readonly settings: SettingsState;
  readonly region: RegionState;
  readonly city: CityState;
  readonly district: DistrictState;
  readonly street: StreetState;
  readonly house: HouseState;
  readonly serviceFee: ServiceFeeState;
  readonly houseTracking: HouseTrackingState;
  readonly housePhoto: HousePhotoState;
  readonly landProject: LandProjectState;
  readonly landProjectPhoto: LandProjectPhotoState;
  readonly article: ArticleState;
  readonly category: CategoryState;
  readonly userProfile: UserProfileState;
  readonly userSubscription: UserSubscriptionState;
  readonly userTracking: UserTrackingState;
  readonly userFeed: UserFeedState;
  readonly searchTracking: SearchTrackingState;
  readonly userFinancial: UserFinancialState;
  readonly potentialCustomer: PotentialCustomerState;
  readonly banner: BannerState;
  readonly payment: PaymentState;
  /* jhipster-needle-add-reducer-type - JHipster will add reducer type here */
  readonly loadingBar: any;
}

const rootReducer = combineReducers<IRootState>({
  authentication,
  locale,
  applicationProfile,
  administration,
  userManagement,
  register,
  activate,
  passwordReset,
  password,
  settings,
  region,
  city,
  district,
  street,
  house,
  serviceFee,
  houseTracking,
  housePhoto,
  landProject,
  landProjectPhoto,
  article,
  category,
  userProfile,
  userSubscription,
  userTracking,
  userFeed,
  searchTracking,
  userFinancial,
  potentialCustomer,
  banner,
  payment,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar
});

export default rootReducer;
