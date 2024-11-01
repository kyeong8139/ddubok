import { Canvas } from "fabric/fabric-impl";
import { IEvent } from "fabric/fabric-impl";
import { fabric } from "fabric";

export interface IBrushComponentProps {
	canvas: Canvas | null;
}

export interface IPathCreatedEvent extends IEvent<Event> {
	path: fabric.Path;
}
