import * as React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, openFile, byteSize } from 'react-jhipster';
import FontAwesomeIcon from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './land-project.reducer';
import { ILandProject } from 'app/shared/model/land-project.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ILandProjectDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: number }> {}

export class LandProjectDetail extends React.Component<ILandProjectDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { landProject } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="landexpApp.landProject.detail.title">LandProject</Translate> [<b>{landProject.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="name">
                <Translate contentKey="landexpApp.landProject.name">Name</Translate>
              </span>
            </dt>
            <dd>{landProject.name}</dd>
            <dt>
              <span id="image">
                <Translate contentKey="landexpApp.landProject.image">Image</Translate>
              </span>
            </dt>
            <dd>
              {landProject.image ? (
                <div>
                  <a onClick={openFile(landProject.imageContentType, landProject.image)}>
                    <img src={`data:${landProject.imageContentType};base64,${landProject.image}`} style={{ maxHeight: '30px' }} />
                  </a>
                  <span>
                    {landProject.imageContentType}, {byteSize(landProject.image)}
                  </span>
                </div>
              ) : null}
            </dd>
            <dt>
              <Translate contentKey="landexpApp.landProject.city">City</Translate>
            </dt>
            <dd>{landProject.cityName ? landProject.cityName : ''}</dd>
            <dt>
              <Translate contentKey="landexpApp.landProject.district">District</Translate>
            </dt>
            <dd>{landProject.districtName ? landProject.districtName : ''}</dd>
            <dt>
              <Translate contentKey="landexpApp.landProject.street">Street</Translate>
            </dt>
            <dd>{landProject.streetName ? landProject.streetName : ''}</dd>
            <dt>
              <Translate contentKey="landexpApp.landProject.createBy">Create By</Translate>
            </dt>
            <dd>{landProject.createByLogin ? landProject.createByLogin : ''}</dd>
            <dt>
              <Translate contentKey="landexpApp.landProject.updateBy">Update By</Translate>
            </dt>
            <dd>{landProject.updateByLogin ? landProject.updateByLogin : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/land-project" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/land-project/${landProject.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ landProject }: IRootState) => ({
  landProject: landProject.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(LandProjectDetail);
