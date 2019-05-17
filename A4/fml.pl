personEnd([X,_,Width,_],PersonEnd):- PersonEnd is X + Width.	
pastPerson(NewX,[X,_,Width,_],PersonEnd):- PersonEnd is X + Width, NewX > PersonEnd.
	
inRoom(X,Y):- X >= 0, X < 13, Y >= 0, Y < 11.

isLevel(LazerHeight,EndHeight):- LazerHeight = EndHeight.

atEnd(LazX,LazY,EndX,EndY):- LazX = EndX, LazY = EndY.

safe([LazX,LazY],[X,Y,Width,Height]):- \+unSafe([LazX,LazY],[X,Y,Width,Height]).

unSafe([LazX,LazY],[X,Y,Width,Height]):- XSize is X + Width, YSize is Y + Height, between(X,XSize,LazX),between(Y,YSize,LazY).

checkSafe([LazX,LazY],[H|T]):- inRoom(LazX,LazY), safe([LazX,LazY],H), checkSafe([LazX,LazY],T).
checkSafe([LazX,LazY],[]):- !.

placeMirror(LazX,LazY,Obsticles,Mirrors):- move([LazX,LazY,0],Obsticles,Mirrors,LazY).
	
move([LazX,LazY,0],_,Mirrors,Emitter) :- atEnd(LazX,LazY,12,Emitter), write(Mirrors), write(" [At End]").				%Test MoveDir && 0

move([LazX,LazY,0],Obsticles,Mirrors,Emitter) :- NewX is LazX + 1, NewY is LazY, checkSafe([NewX,NewY],Obsticles), move([NewX,NewY,0],Obsticles,Mirrors,Emitter).
move([LazX,LazY,0],Obsticles,Mirrors,Emitter) :- NewX is LazX, NewY is LazY + 1, checkSafe([NewX,NewY],Obsticles), append(Mirrors,[[LazX,LazY,//]],NewMirrors), move([NewX,NewY,1],Obsticles,NewMirrors,Emitter).
move([LazX,LazY,0],Obsticles,Mirrors,Emitter) :- NewX is LazX, NewY is LazY - 1, checkSafe([NewX,NewY],Obsticles), append(Mirrors,[[LazX,LazY,\\]],NewMirrors), move([NewX,NewY,-1],Obsticles,NewMirrors,Emitter).

move([LazX,LazY,1],Obsticles,Mirrors,Emitter) :- NewY is LazY + 1, NewX is LazX, checkSafe([NewX,NewY],Obsticles), move([NewX,NewY,1],Obsticles,Mirrors,Emitter).
move([LazX,LazY,1],Obsticles,Mirrors,Emitter) :- NewY is LazY, NewX is LazX + 1, checkSafe([NewX,NewY],Obsticles), append(Mirrors,[[LazX,LazY,//]],NewMirrors), move([NewX,NewY,0],Obsticles,NewMirrors,Emitter).

move([LazX,LazY,-1],Obsticles,Mirrors,Emitter) :- NewY is LazY - 1, NewX is LazX, checkSafe([NewX,NewY],Obsticles), NewX < 12, isLevel(NewY,Emitter), append(Mirrors,[[LazX,NewY,\\]],NewMirrors), move([NewX,NewY,0],Obsticles,NewMirrors,Emitter).
move([LazX,LazY,-1],Obsticles,Mirrors,Emitter) :- NewY is LazY - 1, NewX is LazX, checkSafe([NewX,NewY],Obsticles), move([NewX,NewY,-1],Obsticles,Mirrors,Emitter).
move([LazX,LazY,-1],Obsticles,Mirrors,Emitter) :- NewY is LazY, NewX is LazX + 1, checkSafe([NewX,NewY],Obsticles), append(Mirrors,[[LazX,LazY,\\]],NewMirrors), move([NewX,NewY,0],Obsticles,NewMirrors,Emitter).