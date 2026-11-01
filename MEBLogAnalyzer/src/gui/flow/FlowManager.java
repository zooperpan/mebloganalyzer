package gui.flow;

import java.util.ArrayList;
import java.util.List;

/**
 * The <code>FlowManager</code> class is responsible for managing all the flow objects
 * that reside on a flow panel. It handles the reordering and repainting of the flow objects
 * when the user make mouse actions on the objects, for example when selecting an arrow flow
 * object or when reorders the flow entities.
 * 
 * @author Panos Katsikogiannis
 *
 */
public class FlowManager {

	private FlowArea area;
	private List<FlowObject> objects;
	private List<FlowEntity> entities = null;
	private FlowEntity firstFlowEntity = null;
	private FlowEntity lastFlowEntity = null;
	private int minAreaWidth = 0;
	private static final int SIDES_EMPTY_SPACE = 50;
	private static final int VERTICAL_GAP = 10;
	private static final int MIN_HORIZONTAL_GAB = 250;
	private int totalHeight = 0;

	/**
	 * Creates a new {@link FlowManager} object.
	 * @param area the flow area that the manager will handle
	 */
	public FlowManager(FlowArea area) {
		this.area = area;
		objects = new ArrayList<FlowObject>();
	}

	/**
	 * Called when a new flow object is added in the flow panel
	 * @param object the flow object that was added
	 */
	public void addFlowObject(FlowObject object) {
		if (object instanceof FlowEntity) {
			if (entities == null) {
				entities = new ArrayList<FlowEntity>();
				firstFlowEntity = (FlowEntity)object;
			}
			entities.add((FlowEntity)object);
			lastFlowEntity = (FlowEntity)object;
			minAreaWidth = (entities.size() - 1) * MIN_HORIZONTAL_GAB + (2 * SIDES_EMPTY_SPACE);
		} else {
			objects.add(object);
		}
	}

	/**
	 * Returns true if a flow entity with the specified name exists, false otherwise.
	 * @param name the name of the flow entity to check if exists
	 * @return true if a flow entity with the specified name exists, false otherwise
	 */
	public boolean containsEntity(String name) {
		if (entities == null)
			return false;
		for (FlowEntity entity: entities) {
			if (entity.getTitle().equals(name))
				return true;
		}
		return false;
	}

	/**
	 * Returns the number of flow entities on the flow panel.
	 * @return the number of flow entities on the flow panel.
	 */
	public int getNumOfEntities() {
		if (entities == null)
			return 0;
		else return entities.size();
	}

	/**
	 * Returns the horizontal position of the entity in the specified index.
	 * @param index the position of the flow entity in the list of entities
	 * @return the horizontal position of the entity in the specified index
	 */
	public int getEntityXPos(int index) {
		if (entities == null)
			return -1;
		else return entities.get(index).getXPos();
	}

	/**
	 * Returns the position of the specified entity in the list of entities.
	 * @param entity the entity flow object to get its position
	 * @return the position of the specified entity in the list of entities
	 */
	public int getEntityPosition(FlowEntity entity) {
		if (entities == null)
			return -1;
		for (int i = 0; i < entities.size(); i++) {
			if (entities.get(i) == entity)
				return i;
		}
		return -1;
	}

	/**
	 * Repaints all flow objects handled by the manager.
	 * @param width the width of the flow panel
	 */
	public void repaintObjects(int width) {
		int flowEntitiesCount = 0;
		int entityBoxHeight = 0;
		if (totalHeight > area.getHeight())
			area.setHeight(totalHeight);
		if (minAreaWidth > width)
			area.setWidth(minAreaWidth);
		else area.setWidth(width);
		area.setXPos(area.getWidth() / 2);
		area.setYPos(area.getHeight() / 2);

		int flowEntityStep = ((area.getWidth() - (2 * SIDES_EMPTY_SPACE)) / (entities.size() + 1));
		int posOfFirstFlowEntity = flowEntityStep;
		int posOfLastFlowEntity = flowEntityStep;
		if (entities.size() > 1) {
			posOfFirstFlowEntity = SIDES_EMPTY_SPACE + (firstFlowEntity.getWidth() / 2);
			posOfLastFlowEntity = (area.getWidth() - SIDES_EMPTY_SPACE) - (lastFlowEntity.getWidth() / 2);
			if (entities.size() > 2) {
				flowEntityStep = (posOfLastFlowEntity - posOfFirstFlowEntity) / (entities.size() - 1);
			}
		}
		int flowEntityPos = 0;
		for (FlowEntity entity: entities) {
			entityBoxHeight = entity.getHeight();
			if (totalHeight > entity.getLine().getHeight()) {
				entity.getLine().setHeight(totalHeight);
				entity.getLine().setYPos(totalHeight / 2);
			}
			if ((flowEntitiesCount == entities.size() - 1) && (entities.size() > 1)) {
				entity.setXPos(posOfLastFlowEntity);
				entity.getLine().setXPos(posOfLastFlowEntity);
			} else {
				entity.setXPos(flowEntityPos + posOfFirstFlowEntity);
				entity.getLine().setXPos(flowEntityPos + posOfFirstFlowEntity);
			}
			entity.setYPos(entity.getHeight() / 2);
			flowEntityPos += flowEntityStep;
			flowEntitiesCount++;
		}
		int yPos = entityBoxHeight + VERTICAL_GAP;
		for (FlowObject object: objects) {
			yPos += object.getHeight() / 2;
			if (object instanceof FlowState) {
				for (FlowEntity entity: entities) {
					if (entity.getTitle().equals(((FlowState)object).getComponentTitle())) {
						object.setXPos(entity.getXPos());
						object.setYPos(yPos);
						break;
					}
				}
			} else if (object instanceof FlowMessage) {
				for (FlowEntity entity: entities) {
					if (entity.getTitle().equals(((FlowMessage)object).getComponentTitle())) {
						object.setXPos(entity.getXPos());
						object.setYPos(yPos);
						break;
					}
				}
			} else if (object instanceof FlowArrow) {
				int fromPos = 0;
				int toPos = 0;
				for (FlowEntity entity: entities) {
					if (entity.getTitle().equals(((FlowArrow)object).getFrom())) {
						fromPos = entity.getXPos();
						break;
					}
				}
				for (int i = 0; i < entities.size(); i++) {
					FlowEntity entity = entities.get(i);
					if (entity.getTitle().equals(((FlowArrow)object).getTo())) {
						toPos = entity.getXPos();
						if (fromPos == toPos) {
							if (i == entities.size() - 1) {
								if (entities.size() > 1) {
									toPos = entities.get(i - 1).getXPos();
									((FlowArrow)object).setDirection(FlowArrow.ARROW_DIRECTION_LOOP_LEFT);
								}
							} else {
								if (entities.size() > 1) {
									toPos = entities.get(i + 1).getXPos();
									((FlowArrow)object).setDirection(FlowArrow.ARROW_DIRECTION_LOOP_RIGHT);
								}
							}
						} else if (fromPos > toPos) {
							((FlowArrow)object).setDirection(FlowArrow.ARROW_DIRECTION_LEFT);
						} else {
							((FlowArrow)object).setDirection(FlowArrow.ARROW_DIRECTION_RIGHT);
						}
						break;
					}
				}
				object.setWidth(Math.abs(fromPos - toPos));
				if (fromPos > toPos) {
					object.setXPos(toPos + Math.abs(fromPos - toPos) / 2);
				} else if (fromPos < toPos) {
					object.setXPos(fromPos + Math.abs(fromPos - toPos) / 2);
				}
				object.setYPos(yPos);
			}
			object.repaint();
			if (!(object instanceof FlowEntity))
				yPos += (object.getHeight() / 2) + VERTICAL_GAP;
		}
		totalHeight = yPos;
	}

	/**
	 * Returns the height of the flow panel managed by the manager.
	 * @return the height of the flow panel managed by the manager
	 */
	public int getTotalHeight() {
		return totalHeight;
	}

	/**
	 * Returns the minimum area width of the flow area managed by the manager.
	 * @return the minimum area width of the flow area managed by the manager
	 */
	public int getMinAreaWidth() {
		return minAreaWidth;
	}

	/**
	 * Reorders the entities and repaints all flow objects managed by the manager.
	 * @param entityPos the initial position of the dragged entity
	 * @param width the width of the flow panel
	 * @param endX the current horizontal position of the dragged entity
	 */
	public void reorderEntities(int entityPos, int width, int endX) {
		for (int i = 0; i < entities.size(); i++) {
			if ((entityPos > i) && (entities.get(i).getXPos() > endX)) {
				FlowEntity entity = entities.remove(entityPos);
				entities.add(i, entity);
				break;
			} else if ((entityPos < (entities.size() - 1) - i) && (entities.get((entities.size() - 1) - i).getXPos() < endX)) {
				int nextPos = (entities.size() - 1) - i;
				FlowEntity entity = entities.remove(entityPos);
				entities.add(nextPos, entity);
				break;
			}
		}
		repaintObjects(width);
	}
}
