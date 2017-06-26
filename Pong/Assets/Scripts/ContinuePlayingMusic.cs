using UnityEngine;
using System.Collections;
using System.Collections.Generic;

public class ContinuePlayingMusic : MonoBehaviour {


	// Update is called once per frame
	void Update () {
		DontDestroyOnLoad (gameObject);
	}
}
