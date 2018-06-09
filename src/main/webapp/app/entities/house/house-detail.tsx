import * as React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import FontAwesomeIcon from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './house.reducer';
import { IHouse } from 'app/shared/model/house.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IHouseDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: number }> {}

export class HouseDetail extends React.Component<IHouseDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { house } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="landexpApp.house.detail.title">House</Translate> [<b>{house.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="avatar">
                <Translate contentKey="landexpApp.house.avatar">Avatar</Translate>
              </span>
            </dt>
            <dd>{house.avatar}</dd>
            <dt>
              <span id="actionType">
                <Translate contentKey="landexpApp.house.actionType">Action Type</Translate>
              </span>
            </dt>
            <dd>{house.actionType}</dd>
            <dt>
              <span id="address">
                <Translate contentKey="landexpApp.house.address">Address</Translate>
              </span>
            </dt>
            <dd>{house.address}</dd>
            <dt>
              <span id="money">
                <Translate contentKey="landexpApp.house.money">Money</Translate>
              </span>
            </dt>
            <dd>{house.money}</dd>
            <dt>
              <span id="moneyType">
                <Translate contentKey="landexpApp.house.moneyType">Money Type</Translate>
              </span>
            </dt>
            <dd>{house.moneyType}</dd>
            <dt>
              <span id="acreage">
                <Translate contentKey="landexpApp.house.acreage">Acreage</Translate>
              </span>
            </dt>
            <dd>{house.acreage}</dd>
            <dt>
              <span id="discount">
                <Translate contentKey="landexpApp.house.discount">Discount</Translate>
              </span>
            </dt>
            <dd>{house.discount}</dd>
            <dt>
              <span id="direction">
                <Translate contentKey="landexpApp.house.direction">Direction</Translate>
              </span>
            </dt>
            <dd>{house.direction}</dd>
            <dt>
              <span id="directionBalcony">
                <Translate contentKey="landexpApp.house.directionBalcony">Direction Balcony</Translate>
              </span>
            </dt>
            <dd>{house.directionBalcony}</dd>
            <dt>
              <span id="floor">
                <Translate contentKey="landexpApp.house.floor">Floor</Translate>
              </span>
            </dt>
            <dd>{house.floor}</dd>
            <dt>
              <span id="numberOfFloor">
                <Translate contentKey="landexpApp.house.numberOfFloor">Number Of Floor</Translate>
              </span>
            </dt>
            <dd>{house.numberOfFloor}</dd>
            <dt>
              <span id="bathRoom">
                <Translate contentKey="landexpApp.house.bathRoom">Bath Room</Translate>
              </span>
            </dt>
            <dd>{house.bathRoom}</dd>
            <dt>
              <span id="bedRoom">
                <Translate contentKey="landexpApp.house.bedRoom">Bed Room</Translate>
              </span>
            </dt>
            <dd>{house.bedRoom}</dd>
            <dt>
              <span id="parking">
                <Translate contentKey="landexpApp.house.parking">Parking</Translate>
              </span>
            </dt>
            <dd>{house.parking ? 'true' : 'false'}</dd>
            <dt>
              <span id="furniture">
                <Translate contentKey="landexpApp.house.furniture">Furniture</Translate>
              </span>
            </dt>
            <dd>{house.furniture ? 'true' : 'false'}</dd>
            <dt>
              <span id="landType">
                <Translate contentKey="landexpApp.house.landType">Land Type</Translate>
              </span>
            </dt>
            <dd>{house.landType}</dd>
            <dt>
              <span id="saleType">
                <Translate contentKey="landexpApp.house.saleType">Sale Type</Translate>
              </span>
            </dt>
            <dd>{house.saleType}</dd>
            <dt>
              <span id="fee">
                <Translate contentKey="landexpApp.house.fee">Fee</Translate>
              </span>
            </dt>
            <dd>{house.fee}</dd>
            <dt>
              <span id="feeMax">
                <Translate contentKey="landexpApp.house.feeMax">Fee Max</Translate>
              </span>
            </dt>
            <dd>{house.feeMax}</dd>
            <dt>
              <span id="present">
                <Translate contentKey="landexpApp.house.present">Present</Translate>
              </span>
            </dt>
            <dd>{house.present}</dd>
            <dt>
              <span id="hits">
                <Translate contentKey="landexpApp.house.hits">Hits</Translate>
              </span>
            </dt>
            <dd>{house.hits}</dd>
            <dt>
              <span id="statusType">
                <Translate contentKey="landexpApp.house.statusType">Status Type</Translate>
              </span>
            </dt>
            <dd>{house.statusType}</dd>
            <dt>
              <span id="createAt">
                <Translate contentKey="landexpApp.house.createAt">Create At</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={house.createAt} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="updateAt">
                <Translate contentKey="landexpApp.house.updateAt">Update At</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={house.updateAt} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
            <dt>
              <Translate contentKey="landexpApp.house.city">City</Translate>
            </dt>
            <dd>{house.cityName ? house.cityName : ''}</dd>
            <dt>
              <Translate contentKey="landexpApp.house.district">District</Translate>
            </dt>
            <dd>{house.districtName ? house.districtName : ''}</dd>
            <dt>
              <Translate contentKey="landexpApp.house.street">Street</Translate>
            </dt>
            <dd>{house.streetName ? house.streetName : ''}</dd>
            <dt>
              <Translate contentKey="landexpApp.house.project">Project</Translate>
            </dt>
            <dd>{house.projectName ? house.projectName : ''}</dd>
            <dt>
              <Translate contentKey="landexpApp.house.createBy">Create By</Translate>
            </dt>
            <dd>{house.createByLogin ? house.createByLogin : ''}</dd>
            <dt>
              <Translate contentKey="landexpApp.house.updateBy">Update By</Translate>
            </dt>
            <dd>{house.updateByLogin ? house.updateByLogin : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/house" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/house/${house.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.edit">Edit</Translate>
            </span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ house }: IRootState) => ({
  house: house.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(HouseDetail);
