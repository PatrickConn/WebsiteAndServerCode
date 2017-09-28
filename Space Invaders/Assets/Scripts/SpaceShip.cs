using UnityEngine;
using System.Collections;

public class SpaceShip : MonoBehaviour {

	public float speed = 30;

	public GameObject theBullet;
	void FixedUpdate(){
		float horzMove = Input.GetAxisRaw ("Horizontal");

		GetComponent<Rigidbody2D> ().velocity = new Vector2(horzMove, 0) * speed;
	}


	
	// Update is called once per frame
	void Update () {
		if (Input.GetButtonDown ("Jump"))
			{
			Instantiate (theBullet, transform.position, Quaternion.identity);

			// Play bullet fire sound
			SoundManager.Instance.PlayOneShot (SoundManager.Instance.bulletFire);
		

		}


	}
}
