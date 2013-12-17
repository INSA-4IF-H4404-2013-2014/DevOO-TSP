Demonstration

les grandes parties de l'appli à decrire
	1.  La map
		1.a. Noeud et rue (sens de circulation )
		1.b. Zoom et dezoom
		1.c. Livraisons et couleurs
		
	2. La tournée 
		2.a. expliquer les champs
	
	3. Listes des livraisons
		3.a. decrire le texte
		3.b. tout est bindé
		
	4. Description d'une livraison
		4.a. decrire les champs, surtout schedule
		
	5. Description du fromulaire d ajout
		5.a. list client / newCli
		5.b. adresse
		5.c. plage horaire
		
	6. Affichage special grace au menu
		
//------------------------------------------------------------------------------------
La vraie demo
//------------------------------------------------------------------------------------


1. Lancer l'appli
	1. présenter les boutons sans "cliquer"
		1. Présenter rapidement les boutons avec leur tooltip (passer au dessus d'eux un à un)
		2. Montrer la correspondance avec la barre de menu et évoquer les raccourcis clavier
				NE PAS FAIRE LA DEMO DE CNTL+Q, MEFIEZ VOUS DE CNTL +Z 
				
	2. reproduire une séquence de création
		1. importer map
			1. cliquer sur bouton map
			2. naviguer jusqu'aux maps
			3. prendre la 20*20
			** 1.a.
			** 1.b.			
			
		2. importer tournée
			1. cliquer sur bouton tournée
			2. naviguer jusqu'aux tournées
			3. prendre la 20*20 1
			** 1.c.
			** 2.a.
			
		3. selectionner a tout va
		le but est de montrer quela selection est facile, 
		par plusieurs endroits et donne acces a tel ou tel bouton
			1. cliquer sur une livraison dans la map //TODO
			** 3.a.
			2. cliquer sur une livraison dans la liste //TODO
			** 3.b. 
			** 4.a.
			3. cliquer sur un noeud vide //TODO
			
		4. ajouter une livraison
		malmener le formulaire et malmener la tournee ensuite
			1. cliquer sur le bouton add
			** 5.a.
			** 5.b.
			2. selectionner un client existant //TODO
			3. chercher la merde 
				1. selectionner new
				2. entrer un client deja existant //TODO
				3. cliquer sur ok
				--> erreur client deja existant 
				--> erreur scheduler ? //TODO
			4. entrer un client qui n existe pas encore
				5. cliquer sur ok
				--> erreur scheduler ( begin et fin trop proches)
				** 5.c.
				6. entrer une heure abberrante //TODO
				7. cliquer sur ok
				--> erreur scheduler ( erreur heure)
			8. entrer un vrai scheduler
			9. cliquer sur ok 
			--> recalcul de tournée avec la new liv
			10. recliquer sur add
			11. remplir le schedule //TODO
			12. cliquer sur annuler
			13. recliquer sur add
			14. choisir un client //TODO
			15. remplir un schedule //TODO
			16. cliquer sur ok
			
		5. annuler /refaire
			penser a dire que ceux ci deselectionnent pour eviter des problemes d incoherences
			1. cliquer sur annuler
			2. cliquer sur annuler
			3. cliquer sur refaire
			4. selectionner un noeud //TODO
			5. cliquer sur refaire (deselection)
			6. cliquer sur annuler
			
		6. supprimer
			1. selectionner le premier noeud ajouté //TODO
			2. cliquer sur supprimer
			--> on ne peut plus refaire 
			
		6. montrer les retards
			ajouter un noeud toujours au meme client (celui par defaut)
			et sur une meme plage horaire tres courte //TODO
			
			1. ajouter un noeud NO //TODO
			2. ajouter un noeud SO //TODO
			3. ajouter un noeud SE //TODO
			4. ajouter un noeud NE //TODO
			etc.
			
			constater retard sur livraison et sur tournée.
			
		
			
			
			
			
				
	
		