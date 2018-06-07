import * as React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import FontAwesomeIcon from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './land-projects.reducer';
import { ILandProjects } from 'app/shared/model/land-projects.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ILandProjectsDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: number }> {}

export class LandProjectsDetail extends React.Component<ILandProjectsDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { landProjects } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="landexpApp.landProjects.detail.title">LandProjects</Translate> [<b>{landProjects.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="name">
                <Translate contentKey="landexpApp.landProjects.name">Name</Translate>
              </span>
            </dt>
            <dd>{landProjects.name}</dd>
            <dt>
              <Translate contentKey="landexpApp.landProjects.city">City</Translate>
            </dt>
            <dd>{landProjects.cityName ? landProjects.cityName : ''}</dd>
            <dt>
              <Translate contentKey="landexpApp.landProjects.street">Street</Translate>
            </dt>
            <dd>{landProjects.streetName ? landProjects.streetName : ''}</dd>
            <dt>
              <Translate contentKey="landexpApp.landProjects.createBy">Create By</Translate>
            </dt>
            <dd>{landProjects.createByLogin ? landProjects.createByLogin : ''}</dd>
            <dt>
              <Translate contentKey="landexpApp.landProjects.updateBy">Update By</Translate>
            </dt>
            <dd>{landProjects.updateByLogin ? landProjects.updateByLogin : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/land-projects" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/land-projects/${landProjects.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ landProjects }: IRootState) => ({
  landProjects: landProjects.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(LandProjectsDetail);
