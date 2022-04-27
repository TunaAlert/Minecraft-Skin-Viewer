# Instructions for making a custom Animation
You can make your own custom animations which you can then load by clicking `Pose > Animation > Load Animation` and choosing the file.  
The animation files are xml files.
## Parent node
The parent node is called `animation` and has two properties:  
 - `name` is a string that will be used in the `Pose > Animation` list.
 - `length` is a floating point number representing the default number of seconds it takes to complete one loop of the animation, in other words the period.

## Child nodes
The child nodes represent the body parts of the player character:  
 - `torso`

 - `left-arm`

 - `right-arm`

 - `left-leg`

 - `right-leg`

 - `head` 


Additionally, there are two more nodes used to offset the model:  

 - `offset` displaces the entire model

 - `leg-offset` displaces only the legs

### Body parts and Offsets
The body parts and offsets are equal in their structure.  
Body parts have rotational child nodes named `pitch`, `yaw` and `roll` that represent euler angles. These nodes do not all have to be present.  

The offsets have positional child nodes named `x`, `y` and `z` that represent an offset in meters/blocks. These nodes also do not all have to be present.  

Eachof these transformation nodes contain a list of keyframes modifying their values over time. Optionally, they can also have the following properties:  
 - `length`, which overrides the length of the animation for this particular transformation.
 - `i`, which declares a default interpolation for this particular transformation.

### Keyframes
A `keyframe` node has two properties:
 - `t`, which is the time in seconds that has passed since the start of the loop.
 - `i`, which is an optional node declaring the interpolation type that will be used between the current keyframe and the next one.

The node itself will contain a numeric value.  
Example: `<keyframe t="0.25" i="SMOOTH">-30</keyframe>`

### Interpolation
The default interpolation, if none is specified, is `CONSTANT` for the first keyframe, and for following keyframes whatever the interpolation of the previous keyframe is.  
Here is a list of interpolation modes:
 - `CONSTANT` will maintain the value of the current keyframe until the next keyframe.
 - `LINEAR` will linearly interpolate between the current keyframe and the next one.
 - `SMOOTH` will smoothly interpolate between the current keyframe and the next one using two adjacent extrema of a cosine curve.
 - `SMOOTH_IN` will smoothly interpolate into the next keyframe using a root and adjacent extrema of a cosine curve.
 - `SMOOTH_OUT` will smoothly interpolate out of the current keyframe using a root and adjacent extrema of a cosine curve.

Please keep in mind that the first keyframe is not considered to be the next keyframe after the last keyframe, so if you want an interpolation after the last keyframe to the end of the loop I recommend inserting a keyframe at the time limit of the loop with the desired value.